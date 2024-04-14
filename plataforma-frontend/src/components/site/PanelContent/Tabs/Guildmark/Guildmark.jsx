import React, {useState} from 'react';
import axios from 'axios';
import ReCAPTCHA from 'react-google-recaptcha';
import Configurations from '../../../../../middlewares/Configurations.jsx';
import Swal from 'sweetalert2';
import i18n from "../../../../../lib/i18n.jsx"


function Guildmark() {
    const [selectedImage, setSelectedImage] = useState(null);
    const [guildId, setGuildId] = useState('');
    const [guildPassword, setGuildPassword] = useState('');
    const [recaptcha, setRecaptcha] = useState(null);

    const handleImageChange = (e) => {
        const imageFile = e.target.files[0];
        setSelectedImage(imageFile);
    };

    const handleGuildIdChange = (e) => {
        setGuildId(e.target.value);
    };

    const handleGuildPasswordChange = (e) => {
        setGuildPassword(e.target.value);
    };

    const isBmp = (filename) => {
        return filename.toLowerCase().endsWith('.bmp');
    };

    const is16x12 = () => {
        const img = document.querySelector("#selectedGuildmark");
        return img.naturalWidth === 16 && img.naturalHeight === 12;
    };


    const handleUpload = async () => {
        if(recaptcha === null){
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.error'),
                text: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.selectCaptcha'),
            });
            return;
        }

        if (!selectedImage) {
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.error'),
                text: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.selectImage'),
            });
            return;
        }

        if (!isBmp(selectedImage.name)) {
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.error'),
                text: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.shouldBeBmp'),
            });
            return;
        }

        if(!is16x12()){
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.error'),
                text: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.shouldBe16x12'),
            });
            return;
        }


        const formData = new FormData();
        formData.append('image', selectedImage);
        formData.append('guildId', guildId);
        formData.append('guildPassword', guildPassword);
        formData.append('username', localStorage.getItem('username'));
        formData.append('captcha', recaptcha);

        try {
            const response = await axios.post(
                `${Configurations.getBaseUrl()}/guild/guildmark`,
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                }
            );

            Swal.fire({
                icon: 'success',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.success'),
                text: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.successText'),
            });
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: i18n.t('site.components.PanelContent.tabs.guildmark.handleUpload.error'),
                text: error.response.data.message,
            });
        }
    };

    return (
        <div className='h-full overflow-y-auto'>
            <h1 className="text-2xl text-center font-semibold mb-4 text-white">{i18n.t('site.components.PanelContent.tabs.guildmark.content')}</h1>
            <div className="form-group mb-4">
                <label htmlFor="guildid">{i18n.t('site.components.PanelContent.tabs.guildmark.guildId')}</label>
                <input
                    type="number"
                    id="guildid"
                    name="guildid"
                    placeholder={i18n.t('site.components.PanelContent.tabs.guildmark.guildIdPlaceHolder')}
                    className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300 mt-2"
                    value={guildId}
                    onChange={handleGuildIdChange}
                    required
                />
            </div>

            <div className="form-group mb-4">
                <label htmlFor="guildid">{i18n.t('site.components.PanelContent.tabs.guildmark.guildMasterPassword')}</label>
                <input
                    type="password"
                    id="guildpassword"
                    name="guildpassword"
                    placeholder={i18n.t('site.components.PanelContent.tabs.guildmark.guildMasterPasswordPlaceHolder')}
                    className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300 mt-2"
                    onChange={handleGuildPasswordChange}
                    required
                />
            </div>

            <div className="text-2x1 md:text-left block mb-2 w-full">

                <input
                    className="w-5/6 bg-base-100 p-4 border rounded focus:ring focus:ring-blue-300"
                    type="file"
                    accept="image/*"
                    onChange={handleImageChange}
                />



                {selectedImage && (
                    <div className='container mx-auto float-right w-auto border rounded '>
                        <img
                            src={URL.createObjectURL(selectedImage)}
                            className="w-20 h-auto"
                            id="selectedGuildmark"
                        />
                    </div>
                )}

            </div>


            <ReCAPTCHA
                sitekey={Configurations.getRecaptchaPublicKey()}
                size="normal"
                onChange={(e) => setRecaptcha(e)}
                className="mb-4 mt-4"
            />

            <button className="btn btn-primary w-full py-4" onClick={handleUpload}>
                {i18n.t('site.components.PanelContent.tabs.guildmark.send')}
            </button>
        </div>
    );
}

export default Guildmark;
