package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

class ApplicationController(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok("Hello World")
  }
}