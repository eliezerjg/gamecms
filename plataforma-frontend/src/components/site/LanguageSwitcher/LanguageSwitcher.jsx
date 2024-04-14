import React from "react";
import i18n from "../../../lib/i18n.jsx"

function LanguageSwitcher() {
    const setCurrentLang = (lang) => {
        i18n.changeLanguage(lang).then(r => {
            localStorage.setItem('lang', lang);
            window.location.reload();
        });
    }

    const lang = localStorage.getItem('lang') || 'en';
    const maxImageWitdh = 60;
    return (
        <div className='flex flex-col mr-4 text-center'>
            <label htmlFor="language-switcher">{i18n.t('site.components.LanguageSwitcher.content')}</label>

            <div className="dropdown dropdown-hover mt-1">
                <div tabIndex={0} role="button" className="btn w-24">

                    <img src={`/i18n/${lang}.png`} alt="Portugues" width={maxImageWitdh - 35}/> &nbsp;&nbsp;&nbsp;
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                        <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
                    </svg>

                </div>
                <ul tabIndex={0} className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-24">
                    <li hidden={lang === 'pt'} onClick={() => {setCurrentLang('pt')}} ><img src="/i18n/pt.png" alt="Portugues" width={maxImageWitdh}/></li>
                    <li hidden={lang === 'en'} onClick={() => {setCurrentLang('en')}}><img src="/i18n/en.png" alt="English" width={maxImageWitdh} /></li>
                    <li hidden={lang === 'es'} onClick={() => {setCurrentLang('es')}}><img src="/i18n/es.png" alt="Spanish" width={maxImageWitdh} /></li>
                    <li hidden={lang === 'phi'} onClick={() => {setCurrentLang('phi')}}><img src="/i18n/phi.png" alt="Filipino" width={maxImageWitdh}/></li>
                </ul>
            </div>
        </div>
    )
}

export default LanguageSwitcher