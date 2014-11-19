Garfield
===
[![Build Status](https://travis-ci.org/pathikrit/garfield.svg)](https://travis-ci.org/pathikrit/garfield)
[![Dependency Status](https://www.versioneye.com/user/projects/546b7fcc81010682aa0000d2/badge.svg?style=flat)](https://www.versioneye.com/user/projects/546b7fcc81010682aa0000d2)
[![Codacy Badge](https://www.codacy.com/project/badge/49b2ca67eae84e79983f58591db206bf)](https://www.codacy.com)

[Deployment status](https://dashboard-next.heroku.com/apps/garfield-staging/activity)

---

Setup
---

+ Install [Homebrew](http://brew.sh/):
  ```shell
  xcode-select --install
  ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
  brew doctor
  ```

+ Install developer tools:
  ```shell
  brew install scala sbt ruby activator git heroku-toolbelt caskroom/cask/brew-cask
  gem update --system
  gem install travis
  brew cask install java
  ```

+ Install database:
  ```shell
  brew install postgresql
  # Follow instructions from above step
  createdb main
  ```

+ Clone this repo:
  ```shell
  git clone https://github.com/pathikrit/garfield.git
  cd garfield
  ```

+ Run:
  ```shell
  activator run
  ```
  open <http://localhost:9000> to verify server is running

TODO
---

* Code quality & coverage
* VersionEye
* Codegen
