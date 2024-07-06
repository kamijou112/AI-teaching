import courseApi from "@/api/courseApi";

export default function useSeeAddStudents() {
  const seeStudentsPopus = ref(false);
  const courseId = ref('');
  const addStudentsList = ref();

  const openSeeAddStudentsPopup = (id) => {
    seeStudentsPopus.value = true;
    courseId.value = id;

    getData();
  }

  const getData = async () => {
    const { data } = await courseApi.getCourseStudent(courseId.value);
    addStudentsList.value = data.data
  }



  return {
    addStudentsList,
    seeStudentsPopus,
    openSeeAddStudentsPopup
  }
}
