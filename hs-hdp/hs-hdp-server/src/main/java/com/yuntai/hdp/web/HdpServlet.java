package com.yuntai.hdp.web;

import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.util.HdpCmdHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by denglt on 2015/12/23.
 */
public class HdpServlet extends HttpServlet {


    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //System.out.println(req.getRequestURI());
        PrintWriter out = resp.getWriter();
        String uri = req.getRequestURI();
        String[] paths = uri.split("/");
        String cmd = paths[paths.length - 1].split("[.]")[0];
        try {
            String hosId = req.getParameter("hosId");
            String accessToken = req.getParameter("accessToken");
            if (!cmd.equals("sendData")) {
                hosId = "_";
            }
            if (HospitalManager.checkAccessToken(hosId, accessToken)) {
                Map<String, String> params = new HashMap<>();
                Enumeration<String> names = req.getParameterNames();
                while (names.hasMoreElements()) {
                    String name =  names.nextElement();
                    params.put(name, req.getParameter(name));
                }
                String result = HdpCmdHelper.deal(cmd, params).getBody();
                out.println(result);
                out.flush();
                out.close();
            } else {
                out.println("你无权操作!");
                out.flush();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }


}
