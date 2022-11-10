package no.usn.rygleo.prisjegermobv1.navigasjon

import no.usn.rygleo.prisjegermobv1.R

sealed class BottomNavItem(var title: Int, var icon:Int, var screen_route:String){

    object Hjem : BottomNavItem(R.string.home, R.drawable.ic_home,"hjem")
    object Handleliste: BottomNavItem(R.string.shoppingList,R.drawable.ic_my_network,"my_network")
    object Prissammenligning: BottomNavItem(R.string.post,R.drawable.ic_post,"add_post")
    object OmOss: BottomNavItem(R.string.aboutUs,R.drawable.ic_notification,"omoss")
    object Login: BottomNavItem(R.string.login,R.drawable.ic_job,"login")

}
