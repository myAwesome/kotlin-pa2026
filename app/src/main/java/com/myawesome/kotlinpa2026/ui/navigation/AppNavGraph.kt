package com.myawesome.kotlinpa2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myawesome.kotlinpa2026.ui.editor.PostEditorScreen
import com.myawesome.kotlinpa2026.ui.login.LoginScreen
import com.myawesome.kotlinpa2026.ui.months.MonthListScreen
import com.myawesome.kotlinpa2026.ui.onthisday.OnThisDayScreen
import com.myawesome.kotlinpa2026.ui.post.PostViewScreen
import com.myawesome.kotlinpa2026.ui.posts.PostListScreen
import com.myawesome.kotlinpa2026.ui.recentposts.RecentPostsScreen
import com.myawesome.kotlinpa2026.ui.search.SearchScreen

object Routes {
    const val LOGIN = "login"
    const val RECENT_POSTS = "recent"
    const val MONTHS = "months"
    const val POSTS = "posts/{ym}"
    const val POST_VIEW = "post/{id}"
    const val POST_NEW = "post/new"
    const val POST_EDIT = "post/{id}/edit"
    const val SEARCH = "search"
    const val ON_THIS_DAY = "onthisday"

    fun posts(ym: String) = "posts/$ym"
    fun postView(id: Int) = "post/$id"
    fun postEdit(id: Int) = "post/$id/edit"
}

@Composable
fun AppNavGraph(isDark: Boolean = false, onToggleTheme: () -> Unit = {}) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Routes.RECENT_POSTS) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            })
        }

        composable(Routes.RECENT_POSTS) {
            RecentPostsScreen(
                onPostClick = { id -> navController.navigate(Routes.postView(id)) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onOnThisDayClick = { navController.navigate(Routes.ON_THIS_DAY) },
                onMonthsClick = { navController.navigate(Routes.MONTHS) },
                onNewPostClick = { navController.navigate(Routes.POST_NEW) },
                isDark = isDark,
                onToggleTheme = onToggleTheme
            )
        }

        composable(Routes.MONTHS) {
            MonthListScreen(
                onMonthClick = { ym -> navController.navigate(Routes.posts(ym)) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onOnThisDayClick = { navController.navigate(Routes.ON_THIS_DAY) },
                onNewPostClick = { navController.navigate(Routes.POST_NEW) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Routes.POSTS,
            arguments = listOf(navArgument("ym") { type = NavType.StringType })
        ) { backStack ->
            val ym = backStack.arguments?.getString("ym") ?: return@composable
            PostListScreen(
                ym = ym,
                onPostClick = { id -> navController.navigate(Routes.postView(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Routes.POST_VIEW,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: return@composable
            PostViewScreen(
                postId = id,
                onEditClick = { navController.navigate(Routes.postEdit(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.POST_NEW) {
            PostEditorScreen(
                postId = null,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Routes.POST_EDIT,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: return@composable
            PostEditorScreen(
                postId = id,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onPostClick = { id -> navController.navigate(Routes.postView(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ON_THIS_DAY) {
            OnThisDayScreen(
                onPostClick = { id -> navController.navigate(Routes.postView(id)) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
