package dependencies.modules

object Modules {

    object Common {
        const val AUTHENTICATION_PRESENTATION = ":features:authentication:authentication-presentation"
        const val AUTHENTICATION_DOMAIN = ":features:authentication:authentication-domain"
        const val AUTHENTICATION_DATA = ":features:authentication:authentication-data"

        const val IDENTIFICATION_PRESENTATION = ":features:identification:identification-presentation"

        const val MAP_PRESENTATION = ":features:map:map-presentation"

        const val CORE = ":core"
        const val UTILS = ":common:utils"
        const val DESIGN_SYSTEM = ":common:design-system"
    }
}
