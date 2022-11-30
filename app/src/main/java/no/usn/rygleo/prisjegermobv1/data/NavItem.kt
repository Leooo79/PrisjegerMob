package no.usn.rygleo.prisjegermobv1.data

import no.usn.rygleo.prisjegermobv1.R

/**
 * Modellklasse for å definere items til skuffmeny. Tittel, rute, og ikon.
 */
sealed class NavItem(var title: Int, var icon:Int, var screen_route:String){
    object Handleliste: NavItem(R.string.shoppingList,R.drawable.todo,"my_network")
    object Prissammenligning: NavItem(R.string.priceComparison,R.drawable.analyze,"add_post")
    object OmOss: NavItem(R.string.aboutUs,R.drawable.moreinfo,"omoss")
    object Login: NavItem(R.string.login,R.drawable.login,"login")
    object Kart: NavItem(R.string.map, R.drawable.map, "kart")
}
