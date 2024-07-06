import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router';

// Function to check if the user is signed in
const isSignIn = () => !!localStorage.getItem('isSignIn');


import signIn from '@/views/signIn.vue';

import Layout from '@/layouts/Layout.vue';
import LoginLayout from '@/layouts/LoginLayout.vue';

import teacher from "@/router/teacher.js";
import student from "@/router/student.js";

import { useAside } from "@/store/aside.js";


const routes = [
    {
        path: '/',
        component: Layout,
        children: [
            {
                path: '',
                name: 'teacher',
                children: [
                    ...teacher
                ]
            },
            {
                path: "student",
                name: "student",
                children: [
                    ...student
                ]
            }
        ],
        beforeEnter: (to, from, next) => {
            const user = JSON.parse(localStorage.getItem('user'));
            if (!user) {
                next({ name: 'signIn' });
            } else {
                const key = user.identity;
                const asideStore = useAside();
                if (key === 'isTeacher') {
                    asideStore.setTeacher();
                } else {
                    asideStore.setStudent();
                }
                next();
            }
        },
    },
    {
        path: '/signIn',
        name: 'signIn',
        component: signIn,
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});



router.beforeEach((to, from, next) => {
    const userSignedIn = isSignIn();

    if (!userSignedIn && to.name !== 'signIn') {
        next({ path: '/signIn' });
    } else if (userSignedIn && to.name === 'signIn') {
        next({ name: 'home' });
    } else {
        next();
    }
});

export default router;
