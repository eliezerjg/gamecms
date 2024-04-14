import "./styles.css"
import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import configurationSingleton from "../../../middlewares/Configurations.jsx";
import RequestValidator from "../../../middlewares/Request.jsx";
import Swal from "sweetalert2";
import i18n from "../../../lib/i18n.jsx";
import ReactQuill from "react-quill";
import 'react-quill/dist/quill.snow.css';


function AccountSettingsManagementContent() {
    const [customerData, setCustomerData] = useState(null);

    const GET_NOT_FOUND_TITLE = () => i18n.t('admin.components.AccountSettingsManagementContent.GET_NOT_FOUND_TITLE.content');
    const GET_NOT_FOUND_MESSAGE = () => i18n.t('admin.components.AccountSettingsManagementContent.GET_NOT_FOUND_MESSAGE.content');

    const fetchData = async () => {
        try {
            const response = await axios.get(`${configurationSingleton.getBaseUrl()}/admin/conta`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('admin_jwt')}`,
                },
            });
            setCustomerData(response.data);
        } catch (error) {
            RequestValidator.verify(error, true);
            await Swal.fire({
                icon: 'error',
                title: GET_NOT_FOUND_TITLE(),
                text: GET_NOT_FOUND_MESSAGE(),
            });
        }
    };

    useEffect(() => {
        fetchData();
    }, []);


    const [persistModel, setPersistModel] = useState(null);

    useEffect(() => {
        if (customerData) {
            setPersistModel(customerData);
        }
    }, [customerData]);

    const editorRef = useRef(null);
    const updateCustomerInfos = (modifiedModel) => {
        let requestParams = {
            url: `${configurationSingleton.getBaseUrl()}/admin/conta`,
            method: 'PATCH',
            data: modifiedModel,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('admin_jwt')}`
            }
        };

        axios(requestParams).then(response => {
            Swal.fire(
                i18n.t('admin.components.AccountSettingsManagementContent.updateCustomerInfos.success'),
                i18n.t('admin.components.AccountSettingsManagementContent.updateCustomerInfos.successText'),
                'success'
            );
        }, response => {
            RequestValidator.verify(response, true);
            Swal.fire(
                i18n.t('admin.components.AccountSettingsManagementContent.updateCustomerInfos.error'),
                i18n.t('admin.components.AccountSettingsManagementContent.updateCustomerInfos.errorText') + (response.response?.data?.message || response.message + '.'),
                'error'
            );
        });

    };


    return (
        <div className="w-full mb-64">
            <h2 className="text-2xl font-bold text-white mb-8">{i18n.t('admin.components.AccountSettingsManagementContent.content')}</h2>
            <p className="text-sm font-bold text-yellow-500 mb-8">{i18n.t('admin.components.AccountSettingsManagementContent.warningFieldsMarkedWithRed')}</p>
            <div className="w-[50vw]">

                <label htmlFor="launchConfigs" className="block text-sm font-medium text-red-500 mb-2">
                    {i18n.t('admin.components.AccountSettingsManagementContent.launchConfigs')}
                </label>
                <div className="ml-2 p-4 bg-base-200 mb-14" id="launchConfigs">
                    <div className="mb-4">

                        <label htmlFor="enablePreLaunchModeCheckbox" className="block text-sm font-medium text-red-500 mb-2">
                            {i18n.t('admin.components.AccountSettingsManagementContent.enablePreLaunchMode')}
                        </label>

                        <input
                            id="enablePreLaunchModeCheckbox"
                            type="checkbox"
                            className="checkbox"
                            onChange={(e) => {
                                setPersistModel({...persistModel, enableReleaseDateCounter: e.target.checked});
                            }}
                            defaultChecked={customerData && customerData.enableReleaseDateCounter}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="releaseDate" className="block text-sm font-medium text-red-500">
                            {i18n.t('admin.components.AccountSettingsManagementContent.releaseDate')}
                        </label>

                        <input
                            className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                            id='releaseDate'
                            name='releaseDate'
                            type="datetime-local"
                            defaultValue={
                                customerData &&
                                new Date(customerData['releaseDate']).toISOString().split('T')[0] +
                                'T' + new Date(customerData['releaseDate']).toISOString().split('T')[1].slice(0, -8)
                            }
                            onChange={(e) => {
                                let value = e.target.value;
                                if (value != null) {
                                    setPersistModel({...persistModel, ['releaseDate']: value});
                                }
                            }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="releaseImage"
                               className="block text-sm font-medium text-red-500">{i18n.t('admin.components.AccountSettingsManagementContent.releaseBanner')}</label>

                        <img className="mt-1 p-2 w-full border rounded-md text-white"
                             src={customerData && customerData['releaseImage'] || ''}
                             id="releaseImage"
                        />
                        <input type="file" id="releaseImage" name="releaseImage" accept="image/*"
                               className="mt-1 p-2 w-full border rounded-md text-white"
                               defaultValue={customerData && customerData.releaseImage}
                               onChange={(e) => {
                                   if (e.target.files[0] !== null) {
                                       const reader = new FileReader();
                                       reader.onloadend = (e) => {
                                           setPersistModel({...persistModel, ['releaseImage']: reader.result});
                                           document.getElementById('releaseImage').src = reader.result;
                                       };
                                       reader.readAsDataURL(e.target.files[0]);
                                   }
                               }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="releaseMessage"
                               className="block text-sm font-medium text-red-500 ">{i18n.t('admin.components.AccountSettingsManagementContent.preLaunchMessage')}</label>


                        <ReactQuill
                            theme="snow"
                            value={persistModel && persistModel.releaseMessage || ''}
                            onChange={(value) => setPersistModel({ ...persistModel, ['releaseMessage']: value })}
                        />

                    </div>
                </div>


                <label htmlFor="launchConfigs" className="block text-sm font-medium text-red-500 mt-14">
                    {i18n.t('admin.components.AccountSettingsManagementContent.homepageConfigs')}
                </label>
                <div className="ml-2 p-4 bg-base-200 mb-14" id="homepageConfigs">

                    <div className="mb-4">
                        <label htmlFor="homePageTitle"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.homePageTitle')}</label>
                        <input type="homePageTitle" id="homePageTitle" name="homePageTitle"
                               defaultValue={customerData && customerData.homePageTitle}
                               onChange={(e) => setPersistModel({...persistModel, ['homePageTitle']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>

                    <div className="">
                        <label htmlFor="homePageText"
                               className="block text-sm font-medium  ">{i18n.t('admin.components.AccountSettingsManagementContent.homePageText')}</label>


                        <ReactQuill
                            theme="snow"
                            value={persistModel && persistModel.homePageText || ''}
                            onChange={(value) => setPersistModel({ ...persistModel, ['homePageText']: value })}
                        />

                    </div>

                </div>


                <label htmlFor="launchConfigs" className="block text-sm font-medium text-red-500 mt-14">
                    {i18n.t('admin.components.AccountSettingsManagementContent.customerConfigs')}
                </label>
                <div className="ml-2 p-4 bg-base-200 mb-14">
                    <div className="mb-4">
                        <label htmlFor="email"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.email')}</label>
                        <input type="email" id="email" name="email"
                               defaultValue={customerData && customerData.email}
                               onChange={(e) => setPersistModel({...persistModel, ['email']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="databaseName"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.databaseName')}</label>
                        <input type="text" id="databaseName" name="databaseName"
                               defaultValue={customerData && customerData.databaseName}
                               onChange={(e) => setPersistModel({...persistModel, ['databaseName']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="serverIpv4Address"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.serverIpv4Address')}</label>
                        <input type="text" id="serverIpv4Address" name="serverIpv4Address"
                               defaultValue={customerData && customerData.serverIpv4Address}
                               onChange={(e) => setPersistModel({...persistModel, ['serverIpv4Address']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="serverIpv6Address"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.serverIpv6Address')}</label>
                        <input type="text" id="serverIpv6Address" name="serverIpv6Address"
                               defaultValue={customerData && customerData.serverIpv6Address}
                               onChange={(e) => setPersistModel({...persistModel, ['serverIpv6Address']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="sshKey"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.sshKey')}</label>
                        <input type="text" id="sshKey" name="sshKey"
                               defaultValue={customerData && customerData.sshKey}
                               onChange={(e) => setPersistModel({...persistModel, ['sshKey']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="domainFrontEnd"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.domainFrontEnd')}</label>
                        <input type="text" id="domainFrontEnd" name="domainFrontEnd"
                               defaultValue={customerData && customerData.domainFrontEnd}
                               onChange={(e) => setPersistModel({...persistModel, ['domainFrontEnd']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="domainIntegration"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.domainIntegration')}</label>
                        <input type="text" id="domainIntegration" name="domainIntegration"
                               defaultValue={customerData && customerData.domainIntegration}
                               onChange={(e) => setPersistModel({...persistModel, ['domainIntegration']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>

                </div>


                <label htmlFor="webSiteConfigs" className="block text-sm font-medium text-red-500 mt-14">
                    {i18n.t('admin.components.AccountSettingsManagementContent.webSiteConfigs')}
                </label>
                <div className="ml-2 p-4 bg-base-200 mb-14" id="webSiteConfigs">
                    <div className="mb-4">
                        <label htmlFor="serverFantasyName"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.serverFantasyName')}</label>
                        <input type="text" id="serverFantasyName" name="serverFantasyName"
                               defaultValue={customerData && customerData.serverFantasyName}
                               onChange={(e) => setPersistModel({...persistModel, ['serverFantasyName']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="launcherImage"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.launcherImage')}</label>

                        <img className="mt-1 p-2 w-full border rounded-md text-white"
                             src={customerData && customerData['launcherImage'] || ''}
                             id="launcherImage"
                        />
                        <input type="file" id="launcherImage" name="launcherImage" accept="image/*"
                               className="mt-1 p-2 w-full border rounded-md text-white"
                               defaultValue={customerData && customerData.launcherImage}
                               onChange={(e) => {
                                   if (e.target.files[0] !== null) {
                                       const reader = new FileReader();
                                       reader.onloadend = (e) => {
                                           setPersistModel({...persistModel, ['launcherImage']: reader.result});
                                           document.getElementById('launcherImage').src = reader.result;
                                       };
                                       reader.readAsDataURL(e.target.files[0]);
                                   }
                               }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="favIconImage"
                               className="block text-sm font-medium text-white text-red-500">{i18n.t('admin.components.AccountSettingsManagementContent.favIcon')}</label>

                        <img className="mt-1 p-2 w-1/6 border rounded-md text-white"
                             src={customerData && customerData['favIconImage'] || 'selecione.bmp'}
                             id="favIconImage"
                        />
                        <input type="file" id="favIconImage" name="favIconImage" accept="image/png"
                               className="mt-1 p-2 w-full border rounded-md text-white"
                               defaultValue={customerData && customerData.releaseImage}
                               onChange={(e) => {
                                   if (e.target.files[0] !== null) {
                                       const reader = new FileReader();
                                       reader.onloadend = (e) => {
                                           setPersistModel({...persistModel, ['favIconImage']: reader.result});
                                           document.getElementById('favIconImage').src = reader.result;
                                       };
                                       reader.readAsDataURL(e.target.files[0]);
                                   }
                               }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="logoImage"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.logoImage')}</label>

                        <img className="mt-1 p-2 w-1/6 border rounded-md text-white"
                             src={customerData && customerData['logoImage'] || 'selecione.bmp'}
                             id="logoImage"
                        />
                        <input type="file" id="logoImage" name="logoImage" accept="image/*"
                               className="mt-1 p-2 w-full border rounded-md text-white"
                               defaultValue={customerData && customerData.logoImage}
                               onChange={(e) => {
                                   if (e.target.files[0] !== null) {
                                       const reader = new FileReader();
                                       reader.onloadend = (e) => {
                                           setPersistModel({...persistModel, ['logoImage']: reader.result});
                                           document.getElementById('logoImage').src = reader.result;
                                       };
                                       reader.readAsDataURL(e.target.files[0]);
                                   }
                               }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="backgroundImage"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.backgroundImage')}</label>

                        <img className="mt-1 p-2 w-full border rounded-md text-white"
                             src={customerData && customerData['backgroundImage'] || 'selecione.bmp'}
                             id="backgroundImage"
                        />
                        <input type="file" id="backgroundImage" name="backgroundImage" accept="image/*"
                               className="mt-1 p-2 w-full border rounded-md text-white"
                               defaultValue={customerData && customerData.backgroundImage}
                               onChange={(e) => {
                                   if (e.target.files[0] !== null) {
                                       const reader = new FileReader();
                                       reader.onloadend = (e) => {
                                           setPersistModel({...persistModel, ['backgroundImage']: reader.result});
                                           document.getElementById('backgroundImage').src = reader.result;
                                       };
                                       reader.readAsDataURL(e.target.files[0]);
                                   }
                               }}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="footerText"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.footerText')}</label>
                        <input type="text" id="footerText" name="footerText"
                               defaultValue={customerData && customerData.footerText}
                               onChange={(e) => setPersistModel({...persistModel, ['footerText']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="discordUrl"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.discordUrl')}</label>
                        <input type="text" id="discordUrl" name="discordUrl"
                               defaultValue={customerData && customerData.discordUrl}
                               onChange={(e) => setPersistModel({...persistModel, ['discordUrl']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="facebookUrl"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.facebookUrl')}</label>
                        <input type="text" id="facebookUrl" name="facebookUrl"
                               defaultValue={customerData && customerData.facebookUrl}
                               onChange={(e) => setPersistModel({...persistModel, ['facebookUrl']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="youtubeUrl"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.youtubeUrl')}</label>
                        <input type="text" id="youtubeUrl" name="youtubeUrl"
                               defaultValue={customerData && customerData.youtubeUrl}
                               onChange={(e) => setPersistModel({...persistModel, ['youtubeUrl']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="whatsappUrl"
                               className="block text-sm font-medium text-white">{i18n.t('admin.components.AccountSettingsManagementContent.whatsappUrl')}</label>
                        <input type="text" id="whatsappUrl" name="whatsappUrl"
                               defaultValue={customerData && customerData.whatsappUrl}
                               onChange={(e) => setPersistModel({...persistModel, ['whatsappUrl']: e.target.value})}
                               className="mt-1 p-2 w-full border rounded-md"/>
                    </div>
                </div>



                <button className="bg-blue-500 text-white p-2 rounded-md w-full mt-6 text-2xl"
                        onClick={() => updateCustomerInfos(persistModel)}>
                    {i18n.t('admin.components.AccountSettingsManagementContent.update')}
                </button>
            </div>
        </div>
    )
}

export default AccountSettingsManagementContent;
