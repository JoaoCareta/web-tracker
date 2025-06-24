package com.joao.otavio.design_system.units

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.divider.Divider
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun UnitListItem(
    modifier: Modifier = Modifier,
    unitName: String,
    theme: WebTrackerTheme = WebTrackerTheme,
    isDownloadButtonVisible: Boolean = false,
    onDownloadClick: () -> Unit = {},
    onUpdateClick: () -> Unit = {},
    onEnterCompanyClick: () -> Unit = {}
) {
    val dimensions = LocalDimensions.current
    val paddings = LocalPaddings.current

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.mini),
        shape = RoundedCornerShape(dimensions.xxxSmall),
        colors = CardColors(
            containerColor = theme.background,
            contentColor = theme.background,
            disabledContentColor = theme.background,
            disabledContainerColor = theme.background,
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings.xSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_organization_visit_indicator),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = -(dimensions.xSmall))
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = unitName,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily(Font(R.font.rubik_medium)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = theme.primaryText
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(top = paddings.xSmall),
            )

            if (isDownloadButtonVisible) {
                WebTrackerButton(
                    text = stringResource(R.string.unitListItem_download_unit),
                    onClick = onDownloadClick,
                    leftIcon = painterResource(id = R.drawable.ic_download_glyph),
                    disablePadding = true
                )
            } else {
                WebTrackerButton(
                    text = stringResource(R.string.unitListItem_update_unit),
                    onClick = onUpdateClick,
                    leftIcon = painterResource(id = R.drawable.ic_sync_glyph),
                    disablePadding = true
                )

                Divider(
                    modifier = Modifier.padding(top = paddings.xSmall),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensions.huge)
                        .clickable(onClick = onEnterCompanyClick)
                        .padding(start = paddings.xxxSmall),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.unitListItem_access_unit),
                        style = MaterialTheme.typography.bodyLarge,
                        color = theme.primaryText
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = theme.thirdIcon
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun UnitListItemDownloadPreview() {
    UnitListItem(
        unitName = UNIT_NAME,
        isDownloadButtonVisible = true,
        onEnterCompanyClick = { }
    )
}

@Composable
@Preview
fun UnitListItemSyncPreview() {
    UnitListItem(
        unitName = UNIT_NAME,
        isDownloadButtonVisible = false,
        onEnterCompanyClick = { }
    )
}

@Composable
@Preview
fun UnitListItemDarkDownloadPreview() {
    UnitListItem(
        unitName = UNIT_NAME,
        isDownloadButtonVisible = true,
        onEnterCompanyClick = { },
        theme = DarkTheme()
    )
}

@Composable
@Preview
fun UnitListItemDarkSyncPreview() {
    UnitListItem(
        unitName = UNIT_NAME,
        isDownloadButtonVisible = false,
        onEnterCompanyClick = { },
        theme = DarkTheme()
    )
}

const val UNIT_NAME = "WebTracker Unit"
