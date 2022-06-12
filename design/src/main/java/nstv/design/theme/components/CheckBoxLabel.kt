package nstv.design.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nstv.design.theme.ComposableSheepAnimationsTheme

@Composable
fun CheckBoxLabel(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(modifier = Modifier.clickable { onCheckedChange(!checked) }, text = text)
    }
}

@Preview
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        Column {
            CheckBoxLabel(text = "Checked", checked = true, onCheckedChange = {})
            CheckBoxLabel(text = "UnChecked", checked = false, onCheckedChange = {})
        }
    }
}
