package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.ScreeningService;
import com.movietix.xiazihao.service.impl.ScreeningServiceImpl;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@WebServlet("/screenings/*")
public class ScreeningController extends HttpServlet {
    private final ScreeningService screeningService = new ScreeningServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addScreening(req, resp);
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            try {
                selectScreeningById(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                selectScreeningByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("/status")){
            try {
                setScreeningStatus(req,resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            updateScreening(req,resp);
        }
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            deleteScreeningByIds(req,resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //批量设置场次的状态
    private void setScreeningStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] values = req.getParameterValues("ids");
        List<Integer> ids = ServletUtils.parseReqToList(values, Integer.class);
        int status = Integer.parseInt(req.getParameter("status"));
        log.info("接收到的放映场次ID:{}", ids);
        screeningService.setScreeningStatus(ids,status);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //根据id查询场次信息
    private void selectScreeningById(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String pathInfo = req.getPathInfo();
        int id = Integer.parseInt(pathInfo.substring(1));
        Screening screening = screeningService.selectScreeningById(id);
        log.info("查询到的放映场次信息:{}", screening);
        ServletUtils.sendResponse(resp, Result.success(screening));
    }

    //修改场次信息
    private void updateScreening(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        Screening screening = JsonUtils.parseJson(json, Screening.class);
        log.info("接收到的放映场次信息:{}", screening);
        try {
            screeningService.updateScreening(screening);
            ServletUtils.sendResponse(resp, Result.success());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //条件分页查询放映场次
    private void selectScreeningByPage(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        ScreeningQueryParam param = new ScreeningQueryParam();
        {
            param.setMovieId(
                    req.getParameter("movieId") != null ? Integer.parseInt(req.getParameter("movieId")) : null
            );
            param.setHallId(
                    req.getParameter("hallId") != null ? Integer.parseInt(req.getParameter("hallId")) : null
            );
            param.setMovieTitle(req.getParameter("movieTitle"));
            param.setHallName(req.getParameter("hallName"));
            param.setStartTimeFrom(
                    req.getParameter("startTimeFrom") != null ? LocalDateTime.parse(req.getParameter("startTimeFrom")) : null
            );
            param.setStartTimeTo(
                    req.getParameter("startTimeTo") != null ? LocalDateTime.parse(req.getParameter("startTimeTo")) : null
            );
            param.setMinPrice(
                    req.getParameter("minPrice") != null ? Double.valueOf(req.getParameter("minPrice")) : null
            );
            param.setMaxPrice(
                    req.getParameter("maxPrice") != null ? Double.valueOf(req.getParameter("maxPrice")) : null
            );
            param.setMinSeats(
                    req.getParameter("minSeats") != null ? Integer.parseInt(req.getParameter("minSeats")) : null
            );
            param.setStatus(
                    req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : null
            );
            param.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : param.getPage()
            );
            param.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : param.getPageSize()
            );
        }
        log.info("接收到的查询参数:{}", param);
        PageResult<Screening> pageResult = screeningService.selectScreeningByPage(param);
        log.info("查询到的放映场次信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    //根据id批量删除场次
    private void deleteScreeningByIds(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] values = req.getParameterValues("ids");
        List<Integer> ids = ServletUtils.parseReqToList(values, Integer.class);
        log.info("接收到的放映场次ID:{}", ids);
        screeningService.deleteScreeningByIds(ids);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //添加放映场次
    private void addScreening(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        Screening screening = JsonUtils.parseJson(json, Screening.class);
        log.info("接收到的放映场次信息:{}", screening);
        try {
            screeningService.addScreening(screening);
            ServletUtils.sendResponse(resp, Result.success());
        } catch (SQLException e) {
            ServletUtils.sendResponse(resp, Result.error("添加放映场次失败"));
        }
    }


    //暴露方法
    public void exposeSelectScreeningsByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        this.selectScreeningByPage(req, resp);
    }
}
