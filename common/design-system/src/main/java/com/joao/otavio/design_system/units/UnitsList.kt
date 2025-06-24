package com.joao.otavio.design_system.units

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings

@Composable
fun UnitList(
    units: List<Units>,
    onDownloadClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onEnterUnitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val paddings = LocalPaddings.current
    val sortedUnitsList = units.sortedByDescending { it.isDownloadedUnit }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = paddings.xSmall,
                start = paddings.mini,
                end = paddings.mini
            ),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xxxSmall)
    ) {
        items(
            count = sortedUnitsList.size,
            key = { sortedUnitsList[it].unitUuid }
        ) { index ->
            val unit = sortedUnitsList[index]

            UnitListItem(
                unitName = unit.unitName,
                isDownloadButtonVisible = !unit.isDownloadedUnit,
                onDownloadClick = { onDownloadClick.invoke() },
                onUpdateClick = { onUpdateClick.invoke() },
                onEnterCompanyClick = { onEnterUnitClick.invoke() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitListPreview() {
    val dummyUnits = listOf(
        Units(
            unitUuid = "1",
            unitName = "Unidade 1",
            isDownloadedUnit = false
        ),
        Units(
            unitUuid = "2",
            unitName = "Unidade 2",
            isDownloadedUnit = false
        ),
        Units(
            unitUuid = "3",
            unitName = "Unidade 3",
            isDownloadedUnit = false
        ),
        Units(
            unitUuid = "4",
            unitName = "Unidade 4",
            isDownloadedUnit = false
        ),
        Units(
            unitUuid = "5",
            unitName = "Unidade 5",
            isDownloadedUnit = true
        ),
        Units(
            unitUuid = "6",
            unitName = "Unidade 6",
            isDownloadedUnit = false
        ),
        Units(
            unitUuid = "7",
            unitName = "Unidade 7",
            isDownloadedUnit = true
        ),
        Units(
            unitUuid = "8",
            unitName = "Unidade 8",
            isDownloadedUnit = false
        )
    )

    UnitList(
        units = dummyUnits,
        onDownloadClick = { },
        onUpdateClick = { },
        onEnterUnitClick = { }
    )
}

data class Units(
    val unitUuid: String,
    val unitName: String,
    val isDownloadedUnit: Boolean
)
