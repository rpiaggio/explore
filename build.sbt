
val reactJS = "16.7.0"
val scalaJsReact = "1.5.0"
val SUI = "2.4.1"

parallelExecution in (ThisBuild, Test) := false

cancelable in Global := true

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers in Global += Resolver.sonatypeRepo("public")

addCommandAlias("restartWDS", "; explore/fastOptJS::stopWebpackDevServer; explore/fastOptJS::startWebpackDevServer; ~explore/fastOptJS")

val root =
  project
  .in(file("."))
  .settings(commonSettings: _*)
  .aggregate(explore)

lazy val explore = project
  .in(file("explore"))
  .settings(commonSettings: _*)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    version in webpack                       := "4.41.2",
    version in startWebpackDevServer         := "3.9.0",
    webpackConfigFile in fastOptJS         := Some(sourceDirectory.value / "main" / "webpack" / "dev.webpack.config.js"),
    webpackConfigFile in fullOptJS         := Some(sourceDirectory.value / "main" / "webpack" / "prod.webpack.config.js"),
    webpackMonitoredDirectories            += (resourceDirectory in Compile).value,
    webpackMonitoredDirectories            += (sourceDirectory.value / "main" / "webpack"),
    webpackResources                       := (sourceDirectory.value / "main" / "webpack") * "*.js",
    includeFilter in webpackMonitoredFiles := "*",
    useYarn                                := true,
    webpackBundlingMode in fastOptJS       := BundlingMode.LibraryOnly(),
    webpackBundlingMode in fullOptJS       := BundlingMode.Application,
    test                                   := {},
    emitSourceMaps                         := false,
    // NPM libs for development, mostly to let webpack do its magic
    npmDevDependencies in Compile ++= Seq(
      "postcss-loader"                     -> "3.0.0",
      "autoprefixer"                       -> "9.4.4",
      "url-loader"                         -> "1.1.1",
      "file-loader"                        -> "3.0.1",
      "css-loader"                         -> "2.1.0",
      "style-loader"                       -> "0.23.1",
      "less"                               -> "3.9.0",
      "less-loader"                        -> "4.1.0",
      "webpack-merge"                      -> "4.2.1",
      "mini-css-extract-plugin"            -> "0.5.0",
      "webpack-dev-server-status-bar"      -> "1.1.0",
      "cssnano"                            -> "4.1.8",
      "uglifyjs-webpack-plugin"            -> "2.1.1",
      "html-webpack-plugin"                -> "3.2.0",
      "optimize-css-assets-webpack-plugin" -> "5.0.1",
      "favicons-webpack-plugin"            -> "0.0.9",
      "why-did-you-update"                 -> "1.0.6"
    ),
    npmDependencies in Compile            ++= Seq(
      "react"           -> reactJS,
      "react-dom"       -> reactJS,
      "semantic-ui-less" -> SUI,
      "aladin-lite" -> "0.0.4"
    ),
    libraryDependencies              ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core"       % scalaJsReact,
      "com.github.japgolly.scalajs-react" %%% "extra"      % scalaJsReact,
      "com.github.japgolly.scalajs-react" %%% "test"       % scalaJsReact % Test,
      "org.typelevel" %%% "cats-effect" % "2.0.0",
      "co.fs2"        %%% "fs2-core"    % "2.1.0",
      "io.github.cquiroz.react" %%% "common" % "0.4.2",
      "io.github.cquiroz.react" %%% "react-grid-layout"       % "0.2.1",
      "io.github.cquiroz.react" %%% "react-semantic-ui"       % "0.3.1",
      "io.github.cquiroz.react" %%% "react-sizeme"       % "0.1.1",
      "com.lihaoyi"                       %%% "utest"      % "0.7.3" % Test,
      "org.typelevel"                     %%% "cats-core"  % "2.1.0" % Test
    ),
    // don't publish the demo
    publish                                := {},
    publishLocal                           := {},
    publishArtifact                        := false,
    Keys.`package`                         := file("")
  )

lazy val commonSettings = Seq(
  scalaVersion            := "2.13.1",
  organization            := "io.github.cquiroz",
  description             := "OT demo",
  homepage                := Some(url("https://github.com/cquiroz/scalajs-react-semantic-ui")),
  licenses                := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
  scalacOptions ~= (_.filterNot(Set(
    // By necessity facades will have unused params
    "-Wdead-code",
    "-Wunused:params"
  ))),
  scalacOptions += "-P:scalajs:sjsDefinedByDefault",
)
