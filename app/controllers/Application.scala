package controllers

import db.default.dao
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def ping = Action {
    import dao.PeopleRow
    Ok("ping")
  }
}
