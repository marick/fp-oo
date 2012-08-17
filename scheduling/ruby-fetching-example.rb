require 'rubygems'
require 'sequel'
require 'pp'
require 'shoulda'
require 'set'

DB = Sequel.connect('postgres://postgres@localhost/fpoo')

class FetchingExample < Test::Unit::TestCase

  should "be able to fetch a map of courses" do 
    registration_counts = DB[:courses].
      left_join(:signups, :signups__course_id => :courses__id).
      group(:courses__id).
      select(:courses__id.as(:course_id)).
      select_more{count(:signups__id).as(:registered)}
    
    courses = DB[:courses].
      join(registration_counts, :course_id => :courses__id).
      join(:course_templates, :course_templates__id => :courses__course_template_id).
      select(:course_templates__name.as("course-name"),
             :courses__morning.as("morning?"),
             :limit,
             :registered)

    # puts courses.sql
    pp courses.all

    actual = Set.new(courses.all)

    assert_equal(Set.new([
      {:"course-name" => "Zigging", :morning? => true, :limit => 4, :registered => 0},
      {:"course-name" => "Zigging", :morning? => false, :limit => 4, :registered => 2},
      {:"course-name" => "Zagging", :morning? => true, :limit => 2, :registered => 1},
      {:"course-name" => "Zagging", :morning? => false, :limit => 2, :registered => 1}]),
                 actual)
  end

end

