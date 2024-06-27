package io.margit.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.jline.reader.LineReader;

@Plugin(name = "JLineAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class JLineAppender extends AbstractAppender {
  public static final String PROMPT = "> ";
  private static LineReader reader;

  protected JLineAppender(String name, Layout<? extends Serializable> layout) {
    super(name, null, layout, false, null);
  }

  public static void setReader(LineReader reader) {
    JLineAppender.reader = reader;
  }

  @Override
  public void append(LogEvent event) {
  final String message = new String(getLayout().toByteArray(event));
  System.out.print("\r");
  System.out.print(message);
  System.out.flush();
    if (reader != null) {
      String currentBuffer = reader.getBuffer().toString();
      System.out.print("\r" + PROMPT + currentBuffer);
    }
  }

  @PluginFactory
  public static JLineAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Layout") Layout<? extends Serializable> layout) {
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new JLineAppender(name, layout);
  }
}
