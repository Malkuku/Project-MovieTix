package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.MovieService;
import com.movietix.xiazihao.service.impl.MovieServiceImpl;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@WebServlet("/movies")
public class MovieController extends HttpServlet {

    private final MovieService movieService = new MovieServiceImpl();

    //条件分页查询电影信息
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MovieQueryParam param = new MovieQueryParam();
        {
            param.setTitle(req.getParameter("title"));
            param.setReleaseDate(
                req.getParameter("releaseDate") != null ? LocalDate.parse(req.getParameter("releaseDate")) : null
            );
            param.setGenre(req.getParameter("genre"));
            param.setMinDuration(
                req.getParameter("minDuration") != null ? Integer.parseInt(req.getParameter("minDuration")) : null
            );
            param.setMaxDuration(
                req.getParameter("maxDuration") != null ? Integer.parseInt(req.getParameter("maxDuration")) : null
            );
            param.setMinRating(
                req.getParameter("minRating") != null ? new BigDecimal(req.getParameter("minRating")) : null
            );
            param.setMaxRating(
                req.getParameter("maxRating") != null ? new BigDecimal(req.getParameter("maxRating")) : null
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
        log.info("接收到的电影查询参数:{}", param);
        PageResult<Movie> pageResult = null;
        try {
            pageResult = movieService.selectMoviesByPage(param);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.info("查询到的电影列表:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    //批量删除电影
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] id_strs = req.getParameterValues("ids");
        log.info("接收到的电影ID:{}", (Object) id_strs);
        List<Integer> ids = new ArrayList<>();
        {
            if(id_strs != null) {
                String[] idArray = id_strs[0].split(",");
                for (String id_str : idArray) {
                    log.info("处理ID:{}", id_str);
                    try {
                        ids.add(Integer.parseInt(id_str));
                    } catch (NumberFormatException e) {
                        log.error("ID格式错误:{}", id_str, e);
                    }
                }
            }
        }
        movieService.deleteMoviesByIds(ids);
        ServletUtils.sendResponse(resp, Result.success());
    }
}
