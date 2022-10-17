package no.usn.rygleo.prisjegermobv1.navigasjon

import no.usn.rygleo.prisjegermobv1.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Hjem : BottomNavItem("Hjem", R.drawable.ic_home,"hjem")
    object Handleliste: BottomNavItem("Handleliste",R.drawable.ic_my_network,"my_network")
    object Prissammenligning: BottomNavItem("Post",R.drawable.ic_post,"add_post")
    object OmOss: BottomNavItem("OmOss",R.drawable.ic_notification,"omoss")
    object Login: BottomNavItem("Login",R.drawable.ic_job,"login")
}
