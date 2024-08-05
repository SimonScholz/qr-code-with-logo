package io.github.simonscholz.ui

import io.github.simonscholz.extension.changeItemOnScroll
import io.github.simonscholz.extension.toIntObservable
import io.github.simonscholz.extension.toObservable
import io.github.simonscholz.extension.toSelectedItemObservable
import io.github.simonscholz.model.DotShapes
import io.github.simonscholz.model.QrCodeConfigViewModel
import io.github.simonscholz.ui.properties.BorderPropertiesUI
import io.github.simonscholz.ui.properties.LogoPropertiesUI
import io.github.simonscholz.ui.properties.PositionalSquaresPropertiesUI
import net.miginfocom.swing.MigLayout
import org.eclipse.core.databinding.DataBindingContext
import org.jdesktop.swingx.JXTaskPane
import org.jdesktop.swingx.JXTaskPaneContainer
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.SpinnerNumberModel
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

object PropertiesUI {
    private const val WIDTH = "width 200:220:300"

    fun createPropertiesUI(
        qrCodeConfigViewModel: QrCodeConfigViewModel,
        dataBindingContext: DataBindingContext,
        clickedApply: () -> Unit,
    ): Pair<JComponent, () -> Boolean> {
        configureTaskPaneUI()
        val jxTaskPaneContainer = JXTaskPaneContainer()

        val baseTaskPane =
            JXTaskPane().apply {
                title = "Base"
            }
        baseTaskPane.layout = MigLayout()

        baseTaskPane.add(JLabel("QR Code Content:"))
        val contentTextArea = JTextArea()
        contentTextArea.autoscrolls = true
        contentTextArea.text = "https://simonscholz.dev/"
        baseTaskPane.add(JScrollPane(contentTextArea), "wrap, grow, width 300:300:300, height 200:200:300")
        dataBindingContext.bindValue(contentTextArea.toObservable(), qrCodeConfigViewModel.qrCodeContent)

        baseTaskPane.add(JLabel("Size (px):"))
        val sizeSpinnerModel = SpinnerNumberModel(200, 0, 20000, 1)
        val sizeSpinner = JSpinner(sizeSpinnerModel)
        baseTaskPane.add(sizeSpinner, "wrap, growx, $WIDTH")
        dataBindingContext.bindValue(sizeSpinner.toIntObservable(), qrCodeConfigViewModel.size)

        CustomItems.createColorPickerItem(baseTaskPane, "Background Color:", qrCodeConfigViewModel.backgroundColor, dataBindingContext)
        CustomItems.createColorPickerItem(baseTaskPane, "Foreground Color:", qrCodeConfigViewModel.foregroundColor, dataBindingContext)

        baseTaskPane.add(JLabel("Dot Shape:"))
        val dotShapes = DotShapes.entries.toTypedArray()
        val shapeComboBox = JComboBox(dotShapes)
        shapeComboBox.changeItemOnScroll()
        dataBindingContext.bindValue(shapeComboBox.toSelectedItemObservable(), qrCodeConfigViewModel.dotShape)
        baseTaskPane.add(shapeComboBox, "wrap, growx, span 3, width 200:220:300")

        jxTaskPaneContainer.add(baseTaskPane)

        val logoPropertiesUI = LogoPropertiesUI.createLogoPropertiesUI(dataBindingContext, qrCodeConfigViewModel)
        JXTaskPane().apply {
            title = "Qr Code Logo"
            add(logoPropertiesUI)
            jxTaskPaneContainer.add(this)
        }
        val borderPropertiesUI = BorderPropertiesUI.createBorderPropertiesUI(dataBindingContext, qrCodeConfigViewModel)
        JXTaskPane().apply {
            title = "Qr Code Border"
            isCollapsed = true
            add(borderPropertiesUI)
            jxTaskPaneContainer.add(this)
        }
        val positionalSquaresPropertiesUI =
            PositionalSquaresPropertiesUI.createPositionalSquarePropertiesUI(
                dataBindingContext,
                qrCodeConfigViewModel,
            )
        JXTaskPane().apply {
            title = "Qr Code Positional Squares"
            isCollapsed = true
            add(positionalSquaresPropertiesUI)
            jxTaskPaneContainer.add(this)
        }

        val applyOnChange = JCheckBox("Apply on change")
        applyOnChange.isSelected = true
        jxTaskPaneContainer.add(applyOnChange, "wrap, growx, span 2, gaptop 25")

        val applyButton = JButton("Apply")
        jxTaskPaneContainer.add(applyButton, "wrap, growx, span 2")
        applyButton.addActionListener {
            clickedApply()
        }

        val scrollPane = JScrollPane(jxTaskPaneContainer)
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED)
        scrollPane.viewport.minimumSize = Dimension(500, 0)

        return Pair(scrollPane, applyOnChange::isSelected)
    }

    private fun configureTaskPaneUI() {
        UIManager.getColor("Panel.background")?.let {
            UIManager.put(
                "TaskPane.background",
                it,
            )
            UIManager.put("TaskPaneContainer.background", it)
        }
        UIManager.put("TaskPaneContainer.useGradient", false)

        UIManager.put(
            "TaskPane.font",
            FontUIResource(Font("Verdana", Font.ROMAN_BASELINE, 14)),
        )
        UIManager.put(
            "TaskPane.titleBackgroundGradientStart",
            Color.LIGHT_GRAY.brighter(),
        )
        UIManager.put(
            "TaskPane.titleBackgroundGradientEnd",
            Color.LIGHT_GRAY,
        )
    }
}
