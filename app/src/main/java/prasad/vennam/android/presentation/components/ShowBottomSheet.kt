package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(
    modifier: Modifier,
    sheetContent: @Composable () -> Unit,
) {

    BottomSheetScaffold(
        modifier = modifier,
        sheetContent = {
            sheetContent()
        },
        content = {

        },
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ),
    )
}

@Composable
@Preview
fun ShowBottomSheetPreview() {
    ShowBottomSheet(
        modifier = Modifier,
        sheetContent = {
            ShowDialogPreview()
        }
    )
}
