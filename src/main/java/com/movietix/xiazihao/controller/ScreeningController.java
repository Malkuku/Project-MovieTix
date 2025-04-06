package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.ScreeningService;
import com.movietix.xiazihao.service.impl.ScreeningServiceImpl;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@WebServlet("/screenings/*")
public class ScreeningController extends HttpServlet {
    private final ScreeningService screeningService = new ScreeningServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {

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

    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
                    req.getParameter("minPrice") != null ? new BigDecimal(req.getParameter("minPrice")) : null
            );
            param.setMaxPrice(
                    req.getParameter("maxPrice") != null ? new BigDecimal(req.getParameter("maxPrice")) : null
            );
            param.setMinSeats(
                    req.getParameter("minSeats") != null ? Integer.parseInt(req.getParameter("minSeats")) : null
            );
            param.setStatus(
                    req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : null
            );
            param.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1
            );
            param.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 10
            );
        }
        log.info("接收到的查询参数:{}", param);
        PageResult<Screening> pageResult = null;
        pageResult = screeningService.selectScreeningByPage(param);
        log.info("查询到的放映场次信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

}
