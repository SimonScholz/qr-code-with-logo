package io.github.simonscholz.ui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.Panel
import java.awt.TextArea
import java.awt.TextField
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel

class QrCodeUI : Frame() {
    private val imagePanel: ImagePanel
    private val mainPanel: Panel

    init {
        title = "QR Code AWT UI"
        setSize(800, 600)
        layout = BorderLayout()

        // Create a panel for the BufferedImage on the left
        imagePanel = ImagePanel()
        mainPanel = Panel()
        mainPanel.layout = GridLayout(1, 2)
        mainPanel.add(imagePanel)
        add(mainPanel, BorderLayout.CENTER)

        // Create a panel for properties and controls on the top right corner
        val propertiesPanel = createUIPanel()

        mainPanel.add(propertiesPanel)

        addWindowListener(
            object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent?) {
                    dispose()
                }
            },
        )
    }

    inner class ApplyButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            // You can implement the action to apply properties to the BufferedImage here
        }
    }

    inner class ImagePanel : Panel() {
        private var image: BufferedImage? = null

        init {
            background = Color.WHITE
        }

        fun setImage(image: BufferedImage) {
            this.image = image
            repaint()
        }

        override fun paint(g: Graphics) {
            super.paint(g)
            if (image != null) {
                g.drawImage(image, 0, 0, this)
            }
        }
    }

    private fun createUIPanel(): Panel {
        val panel = Panel()
        panel.layout = GridBagLayout()
        val gbc = GridBagConstraints()

        // First row: "Content" label and TextArea
        val contentLabel = JLabel("Content")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.WEST
        gbc.gridx = 0
        gbc.gridy = 0
        panel.add(contentLabel, gbc)

        val contentTextArea = TextArea(5, 30)
        gbc.fill = GridBagConstraints.BOTH
        gbc.gridx = 1
        panel.add(contentTextArea, gbc)

        // Second row: "Size" label and Integer input field
        val sizeLabel = JLabel("Size")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 1
        panel.add(sizeLabel, gbc)

        val sizeTextField = TextField(10)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(sizeTextField, gbc)

        // Third row: Color group
        val colorGroupLabel = JLabel("Color")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 2
        panel.add(colorGroupLabel, gbc)

        // First row of the Color group: "Background Color" label and Color Picker
        val bgColorLabel = JLabel("Background Color")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 3
        panel.add(bgColorLabel, gbc)

        val bgColorPicker = JButton("Choose Color")
        bgColorPicker.addActionListener {
            val color = JColorChooser.showDialog(this, "Choose a color", Color.WHITE)
            bgColorPicker.background = color
        }
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(bgColorPicker, gbc)

        // Second row of the Color group: "Foreground Color" label and Color Picker
        val fgColorLabel = JLabel("Foreground Color")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 4
        panel.add(fgColorLabel, gbc)

        val fgColorPicker = JButton("Choose Color")
        fgColorPicker.addActionListener {
            val color = JColorChooser.showDialog(this, "Choose a color", Color.WHITE)
            fgColorPicker.background = color
        }
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(fgColorPicker, gbc)

        // Fourth row: Logo group
        val logoGroupLabel = JLabel("Logo")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 5
        panel.add(logoGroupLabel, gbc)

        // First row of the Logo group: "Logo" label, File selection input, and File Dialog button
        val logoLabel = JLabel("Logo")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 6
        panel.add(logoLabel, gbc)

        val logoTextField = TextField(20)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(logoTextField, gbc)

        val chooseFileButton = JButton("Choose File")
        // TODO chooseFileButton.addMouseListener(FileChooserMouseListener(logoTextField))
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 2
        panel.add(chooseFileButton, gbc)

        // Second row of the Logo group: "Relative Size" label and Integer input field
        val relativeSizeLabel = JLabel("Relative Size")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 7
        panel.add(relativeSizeLabel, gbc)

        val relativeSizeTextField = TextField(10)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(relativeSizeTextField, gbc)

        // Third row of the Logo group: "Background Color" label and Color Picker
        val logoBgColorLabel = JLabel("Background Color")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 8
        panel.add(logoBgColorLabel, gbc)

        val logoBgColorPicker = JButton("Choose Color")
        logoBgColorPicker.addActionListener {
            val color = JColorChooser.showDialog(this, "Choose a color", Color.WHITE)
            logoBgColorPicker.background = color
        }
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        panel.add(logoBgColorPicker, gbc)

        return panel
    }
}
