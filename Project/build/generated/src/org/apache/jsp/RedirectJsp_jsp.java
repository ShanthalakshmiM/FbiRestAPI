package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.fb.api.Activities;
import com.fb.api.Constants;
import java.util.ArrayList;

public final class RedirectJsp_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("    </head>\n");
      out.write("    <style>\n");
      out.write("        #textarea{\n");
      out.write("            border:none;\n");
      out.write("            width: 750px;\n");
      out.write("            height: 500px;\n");
      out.write("            display: block;\n");
      out.write("            margin-left: auto;\n");
      out.write("            margin-right: auto;\n");
      out.write("            margin-top: 5%;\n");
      out.write("            font-family: Georgia;\n");
      out.write("            font-size: 24px;\n");
      out.write("        }\n");
      out.write("        .header {\n");
      out.write("            position: absolute;\n");
      out.write("            left: 0;\n");
      out.write("            right: 0;\n");
      out.write("            top: 0;\n");
      out.write("            background-color: #4775d1;\n");
      out.write("            height: 50px;\n");
      out.write("        }\n");
      out.write("        h3{\n");
      out.write("            color: white;\n");
      out.write("        }\n");
      out.write("        p{\n");
      out.write("            color:black;\n");
      out.write("        }\n");
      out.write("    </style>\n");
      out.write("    <body>\n");
      out.write("<!--        <div class=\"header\"> \n");
      out.write("            <h3 align=\"right\" style=\"padding-right: 15px\">Logged in as </h3>  \n");
      out.write("        </div>-->\n");
      out.write("<!--        <div>\n");
      out.write("            <p>\n");
      out.write("                ");
      out.print( request.getParameter("result") );
      out.write("\n");
      out.write("            </p>\n");
      out.write("        </div>\n");
      out.write("       -->\n");
      out.write("    <center>\n");
      out.write("        \n");
      out.write("        ");
 Object obj = Constants.recipient_id;
        Object id = Constants.convoId;

      out.write("\n");
      out.write("        <div>\n");
      out.write("            <p>\n");
      out.write("                ");
      out.print( obj );
      out.write(" <br/>\n");
      out.write("                ");
      out.print( id );
      out.write("\n");
      out.write("                \n");
      out.write("            </p>\n");
      out.write("        </div>\n");
      out.write("        <script>\n");
      out.write("            var convos = ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${result}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(";\n");
      out.write("           // document.writeln(messages);\n");
      out.write("         \n");
      out.write("         // document.writeln(convos[0]);\n");
      out.write("//            document.writeln(\"<textarea id = 'textarea'>\");\n");
      out.write("//            for (var i = 0; i < temp.length; i++) {\n");
      out.write("//                var messages = temp[i]['receivedMsgs'];\n");
      out.write("//                for(var j=0; j<messages.length;j++){\n");
      out.write("//                if(messages[i].postContent != null){\n");
      out.write("//                    document.writeln(\"Post Message : \"+messages[i].postContent);\n");
      out.write("//                }\n");
      out.write("//                if(messages[i].sender!= null || messages[i].content != null)                    \n");
      out.write("//                    document.writeln(messages[i].sender + \" : \" + messages[i].content+\"\\n\");\n");
      out.write("//                }\n");
      out.write("//            }\n");
      out.write("//            document.writeln(messages[0].convId);\n");
      out.write("//            document.writeln(messages[1].convId);\n");
      out.write("//            //document.writeln(\"Global variables :\" + obj );\n");
      out.write("//            document.write(\"</textarea>\");\n");
      out.write("        console.log(convos[0]);\n");
      out.write("            \n");
      out.write("        </script>\n");
      out.write("    </center>\n");
      out.write("           \n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
