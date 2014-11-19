package controllers

import db.default.daos
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def ping = Action {
    import daos.PeopleRow
    Ok("ping")
  }
}
