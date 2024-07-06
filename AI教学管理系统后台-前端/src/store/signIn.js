import { acceptHMRUpdate, defineStore } from "pinia";

export const useIsSignIn = defineStore("signInStore", () => {
  const isSignIn = () => {
    return !!localStorage.getItem("token");
  }

  return { isSignIn };
});

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useIsSignIn, import.meta.hot));
}
