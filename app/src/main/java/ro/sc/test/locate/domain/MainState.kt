package ro.sc.test.locate.domain

data class MainViewState(
    val mainNavigation: MainNavigation? = null
)

sealed class MainNavigation
object Login : MainNavigation()
object Onboarding : MainNavigation()
object Home : MainNavigation()
