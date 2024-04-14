import "./styles.css";
import {Link, useLocation} from "react-router-dom";
import React from "react";
import LanguageSwitcher from "../../site/LanguageSwitcher/LanguageSwitcher.jsx";
import i18n from "../../../lib/i18n.jsx";

function AsideMenu() {
    let items = [
        {
            nome: i18n.t('admin.components.AsideMenu.items.overview'),
            ref: "/admin/overview",
            logo: ''
        },
        {
            nome: i18n.t('admin.components.AsideMenu.items.news'),
            ref: "/admin/news",
            logo: ""
        },
        {
            nome: i18n.t('admin.components.AsideMenu.items.guildmarks'),
            ref: "/admin/guildmarks",
            logo: ""
        },
        {
            nome: i18n.t('admin.components.AsideMenu.items.purchases'),
            ref: "/admin/purchases",
            logo: ""
        },
        {
            nome: i18n.t('admin.components.AsideMenu.items.shop'),
            ref: "/admin/shop",
            logo: ""
        },
        {
            nome: i18n.t('admin.components.AsideMenu.items.configurations'),
            ref: "/admin/settings",
            logo: ""
        }

    ];

    const location = useLocation();

    return (
        <div>
            {/* do a refactoring for responsivity */}
            <div className="drawer drawer-side lg:drawer-open">
                <div className="drawer-side">


                    <label htmlFor="my-drawer-4" aria-label="close sidebar" className="drawer-overlay"></label>

                    <ul className="menu p-4 w-[20vw] min-h-full bg-base-200 text-base-content">


                        <div className='container mx-auto mb-4'>
                            <LanguageSwitcher/>
                        </div>

                        <li className="p-4 bg-base-100 text-3xl text-center text-white "> {i18n.t('admin.components.AsideMenu.content')}</li>
                        <hr className="mb-4"/>
                        {
                            items.map((item, index) => (
                                <li key={index}>
                                    <Link to={item.ref}
                                          className={"text-lg" + (location.pathname.includes(item.ref) ? " text-white" : "")}>{item.logo} {item.nome} </Link>
                                </li>
                            ))
                        }

                        <button className="btn btn-secondary btn-outline h-16 p-4 mt-2" onClick={() => {
                            window.open("/", "_blank");
                        }}>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z" />
                            </svg> &nbsp;
                            {i18n.t('admin.components.AsideMenu.items.perspective')}
                        </button>
                    </ul>

                </div>

            </div>

        </div>
    );
}

export default AsideMenu;
