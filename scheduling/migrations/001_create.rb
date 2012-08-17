require 'pp'

class Creation < Sequel::Migration

  def up
    puts "==== Creating tables"
    DB.create_table :course_templates do
      primary_key :id
      String :name
      int :limit
      String :description
    end

    zigging = DB[:course_templates].insert(:name => "Zigging", :limit => 4, :description => "About zigging")
    zagging = DB[:course_templates].insert(:name => "Zagging", :limit => 2, :description => "About zagging")

    pp DB[:course_templates].all
    
    DB.create_table :courses do
      primary_key :id
      foreign_key :course_template_id, :course_templates
      bool :morning
    end      

    zig_morning = DB[:courses].insert(:course_template_id => zigging, :morning => true)
    zig_afternoon = DB[:courses].insert(:course_template_id => zigging, :morning => false)
    zag_morning = DB[:courses].insert(:course_template_id => zagging, :morning => true)
    zag_afternoon = DB[:courses].insert(:course_template_id => zagging, :morning => false)

    pp DB[:courses].all

    DB.create_table :registrants do 
      primary_key :id
      String :name
    end

    dawn = DB[:registrants].insert(:name => "Dawn")
    paul = DB[:registrants].insert(:name => "Paul")
    sophie = DB[:registrants].insert(:name => "Sophie")

    pp DB[:registrants].all

    DB.create_table :signups do 
      primary_key :id
      foreign_key :registrant_id, :registrants
      foreign_key :course_id, :courses
    end

    DB[:signups].insert(:registrant_id => dawn, :course_id => zig_afternoon)
    DB[:signups].insert(:registrant_id => paul, :course_id => zig_afternoon)
    DB[:signups].insert(:registrant_id => sophie, :course_id => zag_morning)
    DB[:signups].insert(:registrant_id => paul, :course_id => zag_afternoon)

    pp DB[:signups].all

  end


  def down
    puts "==== Dropping all tables"
    DB.drop_table :course_templates
    DB.drop_table :courses
    DB.drop_table :registrants
    DB.drop_table :signups
  end
end
