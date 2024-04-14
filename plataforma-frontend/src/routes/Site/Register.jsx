import React, {useState} from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import ReCAPTCHA from "react-google-recaptcha";
import {Link} from "react-router-dom";
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";
import Configurations from "../../middlewares/Configurations.jsx";
import i18n from "../../lib/i18n.jsx";


const barraCrumbs = [
    {
        type: "a",
        name: "Home",
        ref: "/"
    },
    {
        type: "a",
        name: "Register",
        ref: "/register"
    },
];

const Register = () => {
    const [formData, setFormData] = useState({
        user: '',
        password: '',
        recaptcha: '',
    });

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    let recaptcha;
    const updateRecatpcha = (capturedCaptcha) => {
        recaptcha = capturedCaptcha;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();


        if (/[^a-zA-Z0-9]/.test(formData.password) || /[^a-zA-Z0-9]/.test(formData.user)) {
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.routes.Register.handleSubmit.error'),
                text: i18n.t('site.routes.Register.handleSubmit.shouldHaveOnlyLettersAndNumbers'),
            });
            return;
        }



        try {
            formData.recaptcha = recaptcha;
            const response = await axios.post(`${Configurations.getIntegracaoUrl()}/account/create`, formData, {timeout: 10000});


            Swal.fire(
                i18n.t('site.routes.Register.handleSubmit.success'),
                i18n.t('site.routes.Register.handleSubmit.successText'),
                'success'
            )

        } catch (error) {
            if (error.code !== undefined && error.code === 'ECONNABORTED') {
                Swal.fire(
                    i18n.t('site.routes.Register.handleSubmit.error'),
                    i18n.t('site.routes.Register.handleSubmit.errorRegisterTimeoutText'),
                    'error'
                );
            }
            Swal.fire(
                i18n.t('site.routes.Register.handleSubmit.error'),
                error.response.data.message + '. Status: ' + error.response.status + '.',
                'error'
            );
        }
    };


    return (
        <div className='h-screen overflow-y-auto bg-gray-900'>
            <MenuNavbar/>
            <div className="bg-dark p-2 w-full">
                <BreadCrumbs itensCrumbs={barraCrumbs}/>
            </div>


            <div className="container mx-auto m-12 w-5/6 md:w-2/6 " >

                <form onSubmit={handleSubmit} className='container mx-auto bg-base-200 rounded-lg px-6 py-6' data-theme="dark">
                    <p className='text-center text-white text-3xl  p-4'>
                        {i18n.t('site.routes.Register.content')}
                    </p>
                    <div className="mb-4">
                        <input
                            type="text"
                            name="user"
                            id="user"
                            placeholder={i18n.t('site.routes.Register.usernamePlaceHolder')}
                            className="mt-1 p-2 border rounded-md w-full"
                            value={formData.user}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <input
                            type="password"
                            name="password"
                            id="password"
                            placeholder={i18n.t('site.routes.Register.passwordPlaceHolder')}
                            className="mt-1 p-2 border rounded-lg w-full"
                            value={formData.password}
                            onChange={(e) => {
                                handleChange(e);
                            }}
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <input
                            type="password"
                            name="repassword"
                            id="repassword"
                            placeholder={i18n.t('site.routes.Register.confirmPasswordPlaceHolder')}
                            className="mt-1 p-2 border rounded-md w-full"
                            required
                        />
                    </div>

                    <div className="mb-2">
                        <ReCAPTCHA
                            sitekey={Configurations.getRecaptchaPublicKey()}
                            size="normal"
                            onChange={updateRecatpcha}
                            className="float-left mb-4"
                        />
                    </div>

                    <div className="mb-2">
                        <button type="submit" className="btn btn-primary w-full">
                            {i18n.t('site.routes.Register.createAccount')}
                        </button>


                    </div>

                    <div className="text-center mt-2">
                        <Link to="/download" className="text-white text-center">
                            {i18n.t('site.routes.Register.ifIsRegisteredDownloadNow')}
                        </Link>

                    </div>
                </form>
            </div>

            <Footer/>
        </div>
    );
}

export default Register;
