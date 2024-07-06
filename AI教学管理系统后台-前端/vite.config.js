import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'
import Unocss from "unocss/vite"
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
    base: "./",
    plugins: [
        vue(),
        Unocss(),
        AutoImport({
            imports: [
                'vue',
                'vue-router',
                'pinia',
            ],
            dts: './src/auto-imports.d.ts',
            dirs: [
                "src/store/*",
                'src/composables'
            ],
            resolvers: [ElementPlusResolver()],
        }),
        Components({
            dts: "./src/components.d.ts",
            resolvers: [ElementPlusResolver()],
        }),
    ],
    resolve: {
        alias: {
            "@": "/src",
        },
    },
    server: {
        port: 1234,
        open: true,
    }
})
