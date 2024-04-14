# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
export default {
  // other rules...
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
    project: ['./tsconfig.json', './tsconfig.node.json'],
    tsconfigRootDir: __dirname,
  },
}
```

- Replace `plugin:@typescript-eslint/recommended` to `plugin:@typescript-eslint/recommended-type-checked` or `plugin:@typescript-eslint/strict-type-checked`
- Optionally add `plugin:@typescript-eslint/stylistic-type-checked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and add `plugin:react/recommended` & `plugin:react/jsx-runtime` to the `extends` list


### 설치 라이브러리 목록
    - Vite
    - Typescript
    - framer-motion
    - Zustand
    - Tailwind
    - axios

### 페이지 / 컴포넌트파일 / 경로 설정 완료
    - 구조 예시
    ![Alt text](image.png)


### 메인 글꼴 / 배경 / 글자색
    - 글꼴 : 
        1. 제목 : 세종병원체 https://noonnu.cc/font_page/1288
        2. 카드 & 본문 & 태그 : 프리텐다드 => 기본으로 설정 해둠
        3. 디테일 타이틀 : 태백
    - 배경색 : #232323
    - 글자색 : white