package com.cx.aiteaching.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cx.aiteaching.common.R;
import com.cx.aiteaching.dto.*;
import com.cx.aiteaching.entity.*;
import com.cx.aiteaching.mapper.*;
import com.cx.aiteaching.service.*;
import com.cx.aiteaching.vo.CourseStudentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:19
 */
@RestController
@RequestMapping("/api")
@Tag(name = "教师接口")
public class TeacherController {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private TeacherService teacherService;

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentService studentService;
    @Resource
    private CourseService courseService;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseTasksMapper courseTasksMapper;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private TopicService topicService;
    @Resource
    private StudentkclistMapper studentkclistMapper;
    @Resource
    private StudentkclistService studentkclistService;

    /**
     * 教师登录接口
     * @param loginFrom
     * @return
     */
    @PostMapping("/login")
    @Operation(method = "POST",summary = "教师登录接口")
    public R login(@RequestBody LoginFrom loginFrom){
        Teacher teacher = teacherMapper.selectById(1);
        if (teacher.getUsername().equals(loginFrom.getUsername()) && teacher.getPassword().equals(loginFrom.getPassword())) {
            Map<String,Object> map = new HashMap<>();
            map.put("data",teacher);
            map.put("identity","isTeacher");
           return R.ok("登录成功",map);
        }
        return R.fail("账号或密码错误");
    }

    /**
     * 获取教师信息接口
     * @return
     */
    @GetMapping("/TeacherUserInfo")
    @Operation(method = "GET",summary = "获取教师信息接口")
    public R TeacherUserInfo(){
        Teacher teacher = teacherMapper.selectById(1);
        return R.ok("获取教师信息接口成功",teacher);
    }

    /**
     * 修改教师信息接口
     * @param updateInfo
     * @return
     */
    @PostMapping("/UpdateInfo")
    @Operation(method = "POST",summary = "修改教师信息接口")
    public R UpdateInfo(@RequestBody UpdateInfo updateInfo){
        Teacher teacher = teacherMapper.selectById(1);
        teacher.setName(updateInfo.getName());
        teacher.setUsername(updateInfo.getUsername());
        teacher.setPassword(updateInfo.getPassword());
        int i = teacherMapper.updateById(teacher);
        if (i <= 0) {
            return R.fail("修改教师信息失败");
        }
        return R.ok("修改教师信息成功");
    }


    /**
     * 获取学生列表接口
     * @return
     */
    @GetMapping("/getStudentList")
    @Operation(method = "GET",summary = "获取学生列表接口")
    public R getStudentList(){
        List<Student> list = studentService.list();
        return R.ok("获取学生列表接口成功",list);
    }

    /**
     * 修改学生状态接口
     * @param id
     * @param status
     * @return
     */

    @GetMapping("/updateStudent")
    @Operation(method = "GET",summary = "修改学生状态接口")
    public R updateStudent(Integer id,Integer status){
        Student student = studentMapper.selectById(id);
        student.setStatus(status);
        int i = studentMapper.updateById(student);
        if (i <= 0) {
            return R.fail("修改学生状态失败");
        }
        return R.ok("修改学生状态成功");
    }

    /**
     * 获取课程列表接口
     * @return
     */
    @GetMapping("/getCourseList")
    @Operation(method = "GET",summary = "获取课程列表接口")
    public R getCourseList(){
        List<Course> list = courseService.list();
        return R.ok("获取课程列表接口成功",list);
    }

    /**
     * 创建课程接口
     * @param kcname
     * @return
     */
    @GetMapping("/addCourse")
    @Operation(method = "GET",summary = "创建课程接口")
    public R addCourse(String kcname){
        Course course = new Course();
        course.setKcname(kcname);
        course.setCreatetime(DateTime.now().getTime());
        int insert = courseMapper.insert(course);
        if (insert <= 0) {
            return R.fail("创建课程失败");
        }
        return R.ok("创建课程成功");
    }

    /**
     * 删除课程接口
     * @param id
     * @return
     */
    @GetMapping("/delCourse")
    @Operation(method = "GET",summary = "删除课程接口")
    public  R delCourse(Integer id){
        int i = courseMapper.deleteById(id);
        if (i <= 0) {
            return R.fail("删除课程失败");
        }
        return R.ok("删除课程成功");
    }

    /**
     * 获取单个课程详情
     * @param kcid
     * @return
     */
    @GetMapping("/getCourse")
    @Operation(method = "GET",summary = "获取单个课程详情")
    public R  getCourse(Integer kcid){
        Course course = courseMapper.selectById(kcid);
        return R.ok("获取单个课程详情成功",course);
    }

    /**
     * 查询课程任务列表接口
     * @param kcid
     * @return
     */
    @GetMapping("/getCourseTask")
    @Operation(method = "GET",summary = "查询课程任务列表接口")
    public R getCourseTask(Integer kcid){
        QueryWrapper<CourseTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kcid",kcid);
        List<CourseTasks> courseTasks = courseTasksMapper.selectList(queryWrapper);
        return R.ok(courseTasks);
    }

    /**
     * 创建课程任务接口
     * @param addFrom
     * @return
     */
    @PostMapping("/addCourseTask")
    @Operation(method = "POST",summary = "创建课程任务接口")
    public R addCourseTask(@RequestBody CourseTaskAddFrom addFrom){
        CourseTasks courseTasks = new CourseTasks();
        courseTasks.setTaskname(addFrom.getTaskname());
        courseTasks.setKcid(addFrom.getKcid());
        courseTasks.setType(addFrom.getType());
        courseTasks.setCreatetime(DateTime.now().getTime());

        int insert = courseTasksMapper.insert(courseTasks);
        if (insert <= 0) {
            return R.fail("创建课程任务失败");
        }
        return R.ok("创建课程任务成功");
    }

    /**
     * 删除课程任务接口
     * @param taskid
     * @return
     */
    @GetMapping("/delCourseTask")
    @Operation(method = "GET",summary = "删除课程任务接口")
    public R delCourseTask(Integer taskid){
        int i = courseTasksMapper.deleteById(taskid);
        if (i <= 0) {
            return R.fail("删除课程任务失败");
        }
        return R.ok("删除课程任务成功");
    }

    /**
     * 获取题目列表及详情接口
     * @param taskid
     * @return
     */
    @GetMapping("/getTopicDetile")
    @Operation(method = "GET",summary = "获取题目列表及详情接口")
    public R getTopicDetile(Integer taskid){
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("taskid",taskid);
        List<Topic> topics = topicMapper.selectList(queryWrapper);
        return R.ok(topics);
    }

    /**
     * 创建题目接口
     * @param addFrom
     * @return
     */
    @PostMapping("/AddTopic")
    @Operation(method = "POST",summary = "创建题目接口")
    public R AddTopic(@RequestBody TopicAddFrom addFrom){
        Topic topic = new Topic();
        topic.setTaskid(addFrom.getTaskid());
        topic.setType(addFrom.getType());
        topic.setTigan(addFrom.getTigan());
        topic.setXuanxiang(addFrom.getXuanxiang());
        topic.setDaan(addFrom.getDaan());
        topic.setCreatetime(DateTime.now().getTime());
        int insert = topicMapper.insert(topic);
        if (insert <= 0) {
            return R.fail("创建题目失败");
        }
        return R.ok("创建题目成功");
    }

    /**
     * 编辑题目接口
     * @param updateFrom
     * @return
     */
    @PostMapping("/UpdateTopic")
    @Operation(method = "POST",summary = "编辑题目接口")
    public R UpdateTopic(@RequestBody TopicUpdateFrom updateFrom){
        Topic topic = topicMapper.selectById(updateFrom.getId());
        topic.setTaskid(updateFrom.getTaskid());
        topic.setType(updateFrom.getType());
        topic.setTigan(updateFrom.getTigan());
        topic.setXuanxiang(updateFrom.getXuanxiang());
        topic.setDaan(updateFrom.getDaan());
        int insert = topicMapper.updateById(topic);
        if (insert <= 0) {
            return R.fail("编辑题目失败");
        }
        return R.ok("编辑题目成功");
    }

    /**
     * 删除题目接口
     * @param timuid
     * @return
     */
    @GetMapping("/delTopic")
    @Operation(method = "GET",summary = "删除题目接口")
    public R delTopic(Integer timuid){
        int i = topicMapper.deleteById(timuid);
        if (i <= 0) {
            return R.fail("删除题目失败");
        }
        return R.ok("删除题目成功");
    }

    /**
     * 根据课程获取学生列表
     * @param courseid
     * @return
     */
    @GetMapping("/getCourseStudent")
    @Operation(method = "GET",summary = "根据课程获取学生列表")
    public R getCourseStudent(Integer courseid ){
        List<CourseStudentVo> courseStudent = studentkclistService.getCourseStudent(courseid);
        if (courseStudent == null) {
            return R.fail("此课程暂无学生");
        }
        return R.ok(courseStudent);
    }


    /**
     * 发布课程任务接口
     * @param id
     * @return
     */
    @GetMapping("/updateCourseTask")
    @Operation(method = "GET",summary = "发布课程任务接口")
    public R updateCourseTask(Integer id){
        CourseTasks courseTasks = courseTasksMapper.selectById(id);
        courseTasks.setIsfb(1);
        courseTasksMapper.updateById(courseTasks);
        return R.ok("发布成功");
    }



}
