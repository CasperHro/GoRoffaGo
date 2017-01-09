#
# SonarQube, open source software quality management tool.
# Copyright (C) 2008-2016 SonarSource
# mailto:contact AT sonarsource DOT com
#
# SonarQube is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 3 of the License, or (at your option) any later version.
#
# SonarQube is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program; if not, write to the Free Software Foundation,
# Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
#
require 'digest/sha1'

class User < ActiveRecord::Base

  FAVOURITE_PROPERTY_KEY='favourite'

  has_and_belongs_to_many :groups

  has_many :properties, :foreign_key => 'user_id', :dependent => :delete_all

  include Authentication
  include Authentication::ByPassword
  include NeedAuthorization::ForUser
  
  validates_presence_of :name
  validates_length_of :name, :maximum => 200, :unless => 'name.blank?'

  validates_length_of :email, :maximum => 100, :allow_blank => true, :allow_nil => true

  # The following two validations not needed, because they come with Authentication::ByPassword - see SONAR-2656
  #validates_length_of       :password, :within => 4..40, :if => :password_required?
  #validates_confirmation_of :password, :if => :password_required?

  validates_presence_of :login
  validates_length_of :login, :within => 2..255
  validates_uniqueness_of :login, :case_sensitive => true
  validates_format_of :login, :with => Authentication.login_regex, :message => Authentication.bad_login_message


  # HACK HACK HACK -- how to do attr_accessible from here?
  # prevents a user from submitting a crafted form that bypasses activation
  # anything else you want your user to change should be added here.
  attr_accessible :login, :email, :name, :password, :password_confirmation
  attr_accessor :token_authenticated

  ####
  # As now dates are saved in long they should be no more automatically managed by Rails
  ###

  def record_timestamps
    false
  end

  def before_create
    self.created_at = java.lang.System.currentTimeMillis
    self.updated_at = java.lang.System.currentTimeMillis
  end

  def before_save
    self.updated_at = java.lang.System.currentTimeMillis
  end

  def email=(value)
    write_attribute :email, (value && value.downcase)
  end

  def <=>(other)
    return -1 if name.nil?
    return 1 if other.name.nil?
    name.downcase<=>other.name.downcase
  end

  def self.find_active_by_login(login)
    User.first(:conditions => ["login=:login AND active=:active", {:login => login, :active => true}])
  end


  #---------------------------------------------------------------------
  # USER PROPERTIES
  #---------------------------------------------------------------------
  #
  # This method is different from "set_property(options)" which can also add a new property:
  # it "really" adds a property and does not try to update a existing one with the same key.
  # This is used in the account controller to be able to add notification properties both on
  # a resource (resource_id != nil) or globally (resource_id = nil) - which was not possible
  # with "set_property(options)".
  #
  def add_property(options)
    prop=Property.new(options)
    prop.user_id=id
    properties<<prop
  end

  #---------------------------------------------------------------------
  # FAVOURITES
  #---------------------------------------------------------------------

  def favourite_ids
    @favourite_ids ||=
      begin
        properties().select { |p| p.key==FAVOURITE_PROPERTY_KEY }.map { |p| p.resource_id }
      end
    @favourite_ids
  end

  def favourites
    favourite_ids.size==0 ? [] : Project.find(:all, :conditions => ['id in (?) and enabled=?', favourite_ids, true])
  end

  def add_favourite(resource_key)
    favourite=Project.by_key(resource_key)
    if favourite
      Api::Utils.java_facade.saveProperty(FAVOURITE_PROPERTY_KEY, favourite.id, id, '')
    end
    favourite
  end

  def delete_favourite(resource_key)
    rid=resource_key
    if resource_key.is_a?(String)
      resource=Project.by_key(resource_key)
      rid = resource.id if resource
    end
    if rid
      Api::Utils.java_facade.saveProperty(FAVOURITE_PROPERTY_KEY, rid, id, nil)
      true
    end
    false
  end

  def favourite?(resource_id)
    favourite_ids().include?(resource_id.to_i)
  end


  def notify_creation_handlers
    Java::OrgSonarServerUi::JRubyFacade.getInstance().onNewUser({'login' => self.login, 'name' => self.name, 'email' => self.email})
  end

  # Need to overwrite Authentication::ByPassword#password_required? for SONAR-4064  
  def password_required?
    (crypted_password.blank? && self.new_record?) || !password.blank?
  end

  def self.to_hash(java_user)
    hash = {:login => java_user.login, :name => java_user.name, :active => java_user.active}
    hash[:email] = java_user.email if java_user.email
    hash
  end

  def as_json(options={})
    {
      :login => login,
      :name => name,
      :email => email
    }
  end

  def to_hash
    hash = { :user => self }
    if errors and !errors.empty?
      hash[:errors] = errors.full_messages.map do |msg|
        { :msg => msg }
      end
    end
    hash
  end

  # Copy the method coming from ByCookieToken because it can be used by plugin, for instance by the Active Directory plugin
  def forget_me
    # Nothing to do
  end

end