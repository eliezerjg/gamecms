import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import enTranslations from '../locale/en.json';
import ptTranslations from '../locale/pt.json';
import phiTranslations from '../locale/phi.json';
import esTranslations from '../locale/es.json';


i18n.use(initReactI18next).init({
    resources: {
        en: { translation: enTranslations },
        pt: { translation: ptTranslations },
        es: { translation: esTranslations },
        phi: { translation: phiTranslations }
    },
    lng: localStorage.getItem('lang') || 'en',
    interpolation: {
        escapeValue: false
    }
});

export default i18n;
