import { acceptHMRUpdate, defineStore } from "pinia";
import teacher from "@/router/teacher.js";
import student from "@/router/student.js";


export const useAside = defineStore("asideStore", () => {

    const asideData = ref([]);

    const setTeacher = () => {
        teacher.forEach(i => {
            asideData.value.push({
                name: i.name,
                path: i.path,
                title: i.meta.title,
                children: []
            })
            if (i.children) {
                for (const child of i.children) {
                    asideData.value[asideData.value.length - 1].children.push({
                        name: child.name,
                        path: child.path,
                        title: child.meta.title
                    })
                }
            }
        })


        // asideData.value.unshift({
        //     name: 'home',
        //     path: '/home',
        //     title: '首页',
        //     children: []
        // })
    }

    const setStudent = () => {
        student.forEach(i => {
            if (i.meta.aside) {
                asideData.value.push({
                    name: i.name,
                    path: i.path,
                    title: i.meta.title,
                    children: []
                })
                if (i.children) {
                    for (const child of i.children) {
                        asideData.value[asideData.value.length - 1].children.push({
                            name: child.name,
                            path: child.path,
                            title: child.meta.title
                        })
                    }
                }
            }
        })
    }


    return {
        asideData,
        setTeacher, setStudent
    }
});

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useAside, import.meta.hot));
}
