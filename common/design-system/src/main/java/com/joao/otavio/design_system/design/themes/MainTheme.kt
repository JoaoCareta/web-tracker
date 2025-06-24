package com.joao.otavio.design_system.design.themes

import androidx.compose.ui.graphics.Color
import com.joao.otavio.design_system.design.InternalColors

class MainTheme : WebTrackerTheme {
    override val background by lazy { Color(InternalColors.gray8) }
    override val lightBackground by lazy { Color(InternalColors.white) }

    override val primary by lazy { Color(InternalColors.gray8) }
    override val primaryLight by lazy { Color(InternalColors.green4a) }
    override val primaryDark by lazy { Color(InternalColors.gray6) }
    override val primaryText by lazy { Color(InternalColors.gray1) }
    override val primaryIcon by lazy { Color(InternalColors.green1a) }

    override val secondary by lazy { Color(InternalColors.green1a) }
    override val secondaryDark by lazy { Color(InternalColors.green2a) }
    override val secondaryText by lazy { Color(InternalColors.white) }
    override val secondaryIcon by lazy { Color(InternalColors.gray4) }

    override val third by lazy { Color(InternalColors.green1a) }
    override val thirdDark by lazy { Color(InternalColors.green1a) }
    override val thirdText by lazy { Color(InternalColors.gray3) }
    override val thirdIcon by lazy { Color(InternalColors.gray2) }

    override val outline by lazy { Color(InternalColors.gray4) }
    override val outlineVariant by lazy { Color(InternalColors.gray5) }

    override val secondaryButtonEnabled by lazy { Color(InternalColors.gray6) }

    override val secondaryLineDivider by lazy { Color(InternalColors.gray5) }

    override val neutralStatus by lazy { Color(InternalColors.gray4) }
    override val doneStatus by lazy { Color(InternalColors.acceptance) }
    override val lateStatus by lazy { Color(InternalColors.damageDark) }
    override val error by lazy { Color(InternalColors.damage) }
}
