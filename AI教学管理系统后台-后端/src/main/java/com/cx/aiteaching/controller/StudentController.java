package com.cx.aiteaching.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cx.aiteaching.common.R;
import com.cx.aiteaching.dto.EnrollFrom;
import com.cx.aiteaching.dto.LoginFrom;
import com.cx.aiteaching.dto.UpdateStudent;
import com.cx.aiteaching.entity.*;
import com.cx.aiteaching.mapper.*;
import com.cx.aiteaching.service.CourseService;
import com.cx.aiteaching.service.StudentService;
import com.cx.aiteaching.service.StudentkclistService;
import com.cx.aiteaching.service.TopicDetailsService;
import com.cx.aiteaching.vo.StudentkclistVo;
import com.cx.aiteaching.vo.TaskVo;
import com.cx.aiteaching.vo.TopicDetailsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:57
 */
@RestController
@RequestMapping("/api")
@Tag(name = "学生接口")
public class StudentController {


    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentService studentService;
    @Resource
    private StudentkclistService studentkclistService;
    @Resource
    private StudentkclistMapper studentkclistMapper;
    @Resource
    private TaskLogMapper taskLogMapper;
    @Resource
    private CourseTasksMapper courseTasksMapper;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private TopicDetailsMapper topicDetailsMapper;
    @Resource
    private TopicDetailsService topicDetailsService;

    @Resource
    private CourseService courseService;
    @Resource
    private CourseMapper courseMapper;

    /**
     * 学生账号注册接口
     * @param enrollFrom
     * @return
     */
    @PostMapping("/student/enroll")
    @Operation(method = "POST",summary = "学生账号注册接口")
    public R enroll(@RequestBody EnrollFrom enrollFrom){
        Student student = new Student();
        student.setName(enrollFrom.getName());
        student.setUsername(enrollFrom.getUsername());
        student.setPassword(enrollFrom.getPassword());
        student.setSex(enrollFrom.getSex());
        student.setCreatetime(DateTime.now().getTime());
        int insert = studentMapper.insert(student);
        if (insert <= 0) {
            return R.fail("账号注册失败，请重新尝试");
        }
        return R.ok("账号注册成功，请前去登录");
    }

    /**
     * 学生账号登录接口
     * @param loginFrom
     * @return
     */
    @PostMapping("/student/login")
    @Operation(method = "POST",summary = "学生账号登录接口")
    public R login(@RequestBody LoginFrom loginFrom){
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginFrom.getUsername());
        queryWrapper.eq("password",loginFrom.getPassword());
        Student student = studentMapper.selectOne(queryWrapper);
        if (student == null) {
            return R.fail("账号密码不正确，请重新输入");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",student);
        map.put("identity","isStudent");
        return R.ok("登录成功",map);
    }

    /**
     * 获取学生信息接口
     * @param studentid
     * @return
     */
    @GetMapping("/student/getStudentInfo")
    @Operation(method = "GET",summary = "获取学生信息接口")
    public R getStudentInfo(Integer studentid){
        Student student = studentMapper.selectById(studentid);
        return R.ok("获取学生信息成功",student);
    }


    /**
     * 学生信息修改接口
     * @param updateStudent
     * @return
     */
    @PostMapping("/student/updateStudent")
    @Operation(method = "POST",summary = "学生信息修改接口")
    public R updateStudent(@RequestBody UpdateStudent updateStudent){
        Student student = studentMapper.selectById(updateStudent.getId());
        student.setName(updateStudent.getName());
        student.setUsername(updateStudent.getUsername());
        student.setPassword(updateStudent.getPassword());
        student.setSex(updateStudent.getSex());
        int i = studentMapper.updateById(student);
        if (i <= 0) {
            return R.fail("学生信息修改失败");
        }
        return R.ok("学生信息修改成功");
    }

    /**
     * 获取学生加入的课程列表接口
     * @param studentid
     * @return
     */
    @GetMapping("/student/getCourseListByStudent")
    @Operation(method = "GET",summary = "获取学生加入的课程列表接口")
    public R getCourseListByStudent(Integer studentid){
        List<StudentkclistVo> courseListByStudent = studentkclistService.getCourseListByStudent(studentid);
        return R.ok("学生加入的课程列表获取成功",courseListByStudent);
    }

    /**
     *  模糊查询课程
     * @param wordkey
     * @return
     */
    @GetMapping("/student/getCourse")
    @Operation(method = "GET",summary = "模糊查询课程")
    public R getCourse(String wordkey){
        List<Course> course = courseService.getCourse(wordkey);
        if (course == null) {
            return R.fail("没有查询到有关数据");
        }
        return R.ok(course);
    }

    /**
     * 学生加入课程接口
     * @param studentid
     * @param kcid
     * @return
     */
    @GetMapping("/student/addCourse")
    @Operation(method = "GET",summary = "学生加入课程接口")
    public R addCourse(Integer studentid,Integer kcid){
        QueryWrapper<Studentkclist> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("studentid",studentid);
        queryWrapper.eq("kcid",kcid);
        Studentkclist studentkclist = studentkclistMapper.selectOne(queryWrapper);
        if (studentkclist != null) {
            return R.fail("您以添加过此课程，请勿重复添加");
        }
        Studentkclist studentkclist1 = new Studentkclist();
        studentkclist1.setKcid(kcid);
        studentkclist1.setStudentid(studentid);
        studentkclist1.setCreatetime(DateTime.now().getTime());
        studentkclistMapper.insert(studentkclist1);
        Course course = courseMapper.selectById(kcid);
        course.setStudentcounts(course.getStudentcounts()+1);
        courseMapper.updateById(course);
        return R.ok("课程添加成功");
    }

    /**
     * 学生退出课程接口
     * @param studentid
     * @param kcid
     * @return
     */
    @GetMapping("/student/delCourse")
    @Operation(method = "GET",summary = "学生退出课程接口")
    public R delCourse(Integer studentid,Integer kcid){
        QueryWrapper<Studentkclist> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("studentid",studentid);
        queryWrapper.eq("kcid",kcid);
        int delete = studentkclistMapper.delete(queryWrapper);
        if (delete <= 0) {
            return R.fail("课程退出失败");
        }
        Course course = courseMapper.selectById(kcid);
        course.setStudentcounts(course.getStudentcounts() - 1);
        courseMapper.updateById(course);
        return R.ok("课程退出成功");
    }


    /**
     * 获取课程任务接口
     * @param studentid
     * @param kcid
     * @return
     */
    @GetMapping("/student/getCourseTasks")
    @Operation(method = "GET",summary = "获取课程任务接口")
    public R getCourseTasks(Integer studentid,Integer kcid) {
       QueryWrapper<CourseTasks> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("kcid",kcid);
       queryWrapper.eq("isfb",1);
        List<CourseTasks> courseTasks = courseTasksMapper.selectList(queryWrapper);
        List<TaskVo> list = new ArrayList<>();
        for (CourseTasks courseTask : courseTasks) {
            QueryWrapper<TaskLog> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("taskid",courseTask.getId());
            queryWrapper1.eq("studentid",studentid);
            TaskLog taskLog = taskLogMapper.selectOne(queryWrapper1);
            if (taskLog == null) {
                TaskVo taskVo = new TaskVo();
                taskVo.setTaskid(courseTask.getId());
                taskVo.setTaskname(courseTask.getTaskname());
                taskVo.setStatus(0);
                taskVo.setType(courseTask.getType());
                taskVo.setMsg("任务未完成");
                taskVo.setDatetime(courseTask.getCreatetime());
                taskVo.setData(null);
                list.add(taskVo);
            }else {
                TaskVo taskVo = new TaskVo();
                taskVo.setTaskid(courseTask.getId());
                taskVo.setTaskname(courseTask.getTaskname());
                taskVo.setStatus(1);
                taskVo.setMsg("有该任务");
                taskVo.setType(courseTask.getType());
                taskVo.setDatetime(courseTask.getCreatetime());
                taskVo.setData(courseTask);
                list.add(taskVo);
            }
        }
        return R.ok(list);
    }

    /**
     * 任务开始作答接口
     * @param studentid
     * @param taskid
     * @return
     */
    @GetMapping("/student/Answer")
    @Operation(method = "GET",summary = "任务开始作答接口")
    public R Answer(Integer studentid,Integer taskid){
        QueryWrapper<TaskLog> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("studentid",studentid);
        queryWrapper.eq("taskid",taskid);
        TaskLog taskLog = taskLogMapper.selectOne(queryWrapper);
        if (taskLog == null) {
            TaskLog taskLog1 = new TaskLog();
            taskLog1.setStudentid(studentid);
            taskLog1.setTaskid(taskid);
            taskLog1.setCreatetime(DateTime.now().getTime());
            taskLogMapper.insert(taskLog1);
            QueryWrapper<TaskLog> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("studentid",studentid);
            queryWrapper1.eq("taskid",taskid);
            TaskLog taskLog2 = taskLogMapper.selectOne(queryWrapper1);
            return R.ok("任务进度id已创建",taskLog2);
        }
        return R.ok("已存在任务id",taskLog);
    }

    /**
     * 获取任务进度详情接口
     * @param tasklogid
     * @return
     */
    @GetMapping("/student/MissionDetails")
    @Operation(method = "GET",summary = "获取任务进度详情接口")
    public R MissionDetails( Integer tasklogid){
        TaskLog taskLog = taskLogMapper.selectById(tasklogid);
        if (taskLog == null) {
            return R.fail("暂无此任务进度");
        }
        //ai_task 表
        CourseTasks courseTasks = courseTasksMapper.selectById(taskLog.getTaskid());
        //
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("taskid",courseTasks.getId());
        // topic 题目表
        List<Topic> topics = topicMapper.selectList(queryWrapper);
        for (Topic topic : topics) {
            QueryWrapper<TopicDetails> queryWrapper1 = new QueryWrapper<>();

            queryWrapper1.eq("tasklogid",tasklogid);
            queryWrapper1.eq("timuid",topic.getId());
            TopicDetails topicDetails = topicDetailsMapper.selectOne(queryWrapper1);
            if (topicDetails == null) {
                TopicDetails topicDetails1 = new TopicDetails();
                topicDetails1.setTasklogid(tasklogid);
                topicDetails1.setTimuid(topic.getId());
                topicDetails1.setCreatetime(DateTime.now().getTime());
                topicDetailsMapper.insert(topicDetails1);
            }
        }
        List<TopicDetailsVo> topicDetailsVos = topicDetailsService.topicDetatail(courseTasks.getId(),tasklogid);
        Map<String,Object> map = new HashMap<>();
        map.put("data",topicDetailsVos);
        map.put("course",courseTasks);
        map.put("taskLog",taskLog);
        return R.ok(map);

    }

    /**
     * 作答题目接口
     * @param timuid
     * @param content
     * @return
     */
    @GetMapping("/student/updateAnswer")
    @Operation(method = "GET",summary = "作答题目接口")
    public R updateAnswer(Integer timuid,String content,Integer tasklogid){
        if (content == null || content.equals("") || content.equals("null")) {
            return R.fail("内容不能为空");
        }
        QueryWrapper<TopicDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("timuid",timuid);
        queryWrapper.eq("tasklogid",tasklogid);
        TopicDetails topicDetails = topicDetailsMapper.selectOne(queryWrapper);
        topicDetails.setZuoda(content);
        int i = topicDetailsMapper.updateById(topicDetails);
        if (i <= 0) {
            return R.fail("答题失败");
        }
        return R.ok("答题成功");
    }

    /**
     * 作答完成提交接口
     * @param tasklog
     * @return
     */
    @GetMapping("/student/AnswerSubmission")
    @Operation(method = "GET",summary = "作答完成提交接口")
    public R AnswerSubmission(Integer tasklog){
        TaskLog taskLog = taskLogMapper.selectById(tasklog);
        taskLog.setStatus(2);
        int i = taskLogMapper.updateById(taskLog);
        if (i <= 0) {
            return R.fail("提交失败");
        }

        return R.ok("提交成功");
    }





}
