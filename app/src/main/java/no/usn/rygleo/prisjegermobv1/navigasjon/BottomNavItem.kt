package no.usn.rygleo.prisjegermobv1.navigasjon

import no.usn.rygleo.prisjegermobv1.R

sealed class BottomNavItem(var title: Int, var icon:Int, var screen_route:String){

    object Hjem : BottomNavItem(R.string.home, R.drawable.ic_home,"hjem")
    object Handleliste: BottomNavItem(R.string.shoppingList,R.drawable.todo,"my_network")
    object Prissammenligning: BottomNavItem(R.string.priceComparison,R.drawable.analyze,"add_post")
    object OmOss: BottomNavItem(R.string.aboutUs,R.drawable.moreinfo,"omoss")
    object Login: BottomNavItem(R.string.login,R.drawable.login,"login")
    object Kart: BottomNavItem(R.string.map, R.drawable.map, "kart")


}
