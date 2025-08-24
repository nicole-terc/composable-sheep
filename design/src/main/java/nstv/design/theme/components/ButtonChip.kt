package nstv.design.theme.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StartStopBehaviorButton(
    isBehaviorActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit
) {
    val buttonColors =
        ButtonDefaults.buttonColors(
            containerColor = if (isBehaviorActive) colors.containerColor else colors.disabledContainerColor,
            contentColor = if (isBehaviorActive) colors.contentColor else colors.disabledContentColor,
        )

    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = buttonColors,
    ) {
        content()
    }
}
