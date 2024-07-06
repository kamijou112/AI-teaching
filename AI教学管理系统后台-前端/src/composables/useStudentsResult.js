export default function useStudentsResult() {
  const studentsResultPopup = ref(false);
  const studentsResultData = ref("")

  const openResultPopup = (data) => {
    studentsResultPopup.value = true;
    studentsResultData.value = data
  }


  return {
    studentsResultPopup,
    studentsResultData,
    openResultPopup
  }
}
