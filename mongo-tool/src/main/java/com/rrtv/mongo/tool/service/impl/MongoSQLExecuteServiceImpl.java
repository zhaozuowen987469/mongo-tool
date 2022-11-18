package com.rrtv.mongo.tool.service.impl;

import com.rrtv.mongo.tool.service.MongoSQLExecuteService;
import com.rrtv.mongo.tool.vo.result.ExecuteSQLResultVo;
import com.rrtv.mongo.tool.vo.result.NotQueryExecuteSQLResultVo;
import com.rrtv.mongo.tool.vo.result.QueryExecuteSQLResultVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqltomongo.SQLToMongoTemplate;
import org.sqltomongo.exception.SqlParserException;
import org.sqltomongo.parser.ProjectSQLParser;
import org.sqltomongo.parser.data.ProjectData;
import org.sqltomongo.parser.util.ProjectSQLParserUtil;
import org.sqltomongo.util.SqlCommonUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname MongoSQLExecuteServiceImpl
 * @Description
 * @Date 2022/7/15 16:59
 * @Created by wangchangjiu
 */
@Slf4j
@Service
public class MongoSQLExecuteServiceImpl implements MongoSQLExecuteService {

    @Autowired
    private SQLToMongoTemplate sqlToMongoTemplate;

    @Override
    public ExecuteSQLResultVo executeSql(String sql) {

        SqlCommonUtil.SqlType sqlType;
        try {
            sqlType = SqlCommonUtil.getSqlType(sql);
        } catch (JSQLParserException exception) {
            throw new SqlParserException("解析失败：" + exception);
        }

        if (sqlType == SqlCommonUtil.SqlType.SELECT) {
            // 查询
            return selectResult(sql);
        } else {
            return execute(sql);
        }
    }

    private NotQueryExecuteSQLResultVo execute(String sql) {
        return null;
    }

    private QueryExecuteSQLResultVo selectResult(String sql) {
        List<String> columns;
        List<Map> data;
        try {
            data = sqlToMongoTemplate.selectList(sql, Map.class);
            PlainSelect plainSelect = SqlCommonUtil.parserSelectSql(sql);
            // 解析 投影字段
            List<ProjectData> projectData = ProjectSQLParserUtil.parser(plainSelect.getSelectItems());
            columns = projectData.stream().map(item -> StringUtils.isNotEmpty(item.getAlias()) ? item.getAlias() : item.getField()).collect(Collectors.toList());
            data = data.stream().map(item -> {
                if(item.containsKey("_id") && item.get("_id") != null){
                    Object id = item.get("_id");
                    item.put("_id", id.toString());
                }
                return item;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("执行SQL查询失败：{}", ex);
            return QueryExecuteSQLResultVo.fail(ex.getMessage());
        }
        return QueryExecuteSQLResultVo.success(columns, data);
    }


}
