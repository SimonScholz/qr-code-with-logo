package io.github.simonscholz.components

import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder

class JCollapsiblePanel(
    title: String,
    titleColor: Color = Color.black,
) : JPanel() {
    private val border = TitledBorder("▼ $title")
    private var visibleSize: Dimension? = null

    init {
        border.titleColor = titleColor
        border.border = LineBorder(Color.white)
        setBorder(border)
        // as Titleborder has no access to the Label we fake the size data ;)
        val l = JLabel("▼ $title")
        val size = l.preferredSize
        addMouseListener(
            object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    val i = getBorder().getBorderInsets(this@JCollapsiblePanel)
                    if (e.x < i.left + size.width && e.y < i.bottom + size.height) {
                        if (visibleSize == null || height > size.height) {
                            visibleSize = getSize()
                        }
                        if (getSize().height < visibleSize!!.height) {
                            maximumSize = Dimension(visibleSize!!.width, 20000)
                            minimumSize = visibleSize
                            l.text = "▼ $title"
                            border.title = "▼ $title"
                        } else {
                            maximumSize = Dimension(visibleSize!!.width, size.height)
                            l.text = "▶ $title"
                            border.title = "▶ $title"
                        }
                        revalidate()
                        e.consume()
                    }
                }
            },
        )
    }
}
