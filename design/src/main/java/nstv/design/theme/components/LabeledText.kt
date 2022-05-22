package nstv.design.theme.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LabeledText(label: String, body: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                MaterialTheme.typography.titleMedium.toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.onBackground)
            ) {
                append(label)
            }
            withStyle(MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                append(body)
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    LabeledText(label = "Label: ", body = "body body")
}
