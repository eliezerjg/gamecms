import React, {useState} from 'react';
import axios from 'axios';
import "./styles.css";
import ReCAPTCHA from "react-google-recaptcha";
import Swal from "sweetalert2";
import configurationSingleton from "../../../middlewares/Configurations.jsx";
import Configurations from "../../../middlewares/Configurations.jsx";
import i18n from "../../../lib/i18n.jsx";

function ModalLogin({onLoginSuccess}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    let recaptcha;
    const updateRecatpcha = (capturedCaptcha) => {
        recaptcha = capturedCaptcha;
    };

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post(`${configurationSingleton.getIntegracaoUrl()}/account/login`, {
                user: username,
                password: password,
                recaptcha: recaptcha
            }, {
                timeout: 10000
            });

            localStorage.setItem('username', username);
            localStorage.setItem('isLoggedIn', true);
            localStorage.setItem('jwt', response.data.jwt);

            onLoginSuccess({username});

        } catch (error) {
            if (error.code !== undefined && error.code === 'ECONNABORTED') {
                Swal.fire(
                    'Não foi possível logar =(',
                    'Tempo máximo de requisição atingido, contacte o administrador.',
                    'error'
                );
            }
            Swal.fire(
                'Não foi possível logar =(',
                error.response.data.message + ' Status: ' + error.response.status + '.',
                'error'
            );
        }
    };

    return (
        <div className="modal-box">
            <a href="#">
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2 text-3x2">✕</button>
            </a>

            <form onSubmit={handleLogin}>
                <div className="mb-4">
                    <input
                        type="text"
                        id="username"
                        name="username"
                        placeholder={i18n.t('site.components.ModalLogin.username')}
                        className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className="mb-4">
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder={i18n.t('site.components.ModalLogin.password')}
                        className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>

                <ReCAPTCHA
                    sitekey={Configurations.getRecaptchaPublicKey()}
                    size="normal"
                    onChange={updateRecatpcha}
                    className="mb-4"
                />
                <button type="submit" className="btn btn-secondary w-full">{i18n.t('site.components.ModalLogin.login')}</button>
            </form>
        </div>
    );
}

export default ModalLogin;
