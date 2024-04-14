import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import configurationSingleton from '../../../middlewares/Configurations.jsx';
import DynamicFormModal from "../DynamicFormModal/DynamicFormModal.jsx";
import Loading from "../../site/Loading/Loading.jsx";
import "./styles.css"
import RequestValidator from "../../../middlewares/Request.jsx";
import i18n from "../../../lib/i18n.jsx";

function GuildmarksManagementContent() {
    const [dynamicTableData, setDynamicTableData] = useState({
        cols: [],
        rows: [],
        entriesModel: []
    });
    const SUBJECT_TARGET_NAME = 'Guildmarks';
    const GET_NOT_FOUND_MESSAGE = (TARGET = SUBJECT_TARGET_NAME) => {
        return i18n.t('admin.components.GuildmarksManagement.GET_NOT_FOUND_MESSAGE.content') + TARGET + '.';
    };
    const GET_NOT_FOUND_TITLE = () => {
        return i18n.t('admin.components.GuildmarksManagement.GET_NOT_FOUND_TITLE.content');
    };
    const fetchData = async () => {
        try {
            const response = await axios.get(`${configurationSingleton.getBaseUrl()}/admin/guildmarks`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('admin_jwt')}`,
                },
            });
            console.log(response);
            setDynamicTableData(response.data);
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

    const deleteRow = (id) => {
        const guildId = id;
        Swal.fire({
            title: "<strong>"+i18n.t('admin.components.GuildmarksManagement.deleteRow.confirm')+"</strong>",
            showCloseButton: true,
            showCancelButton: true,
            focusConfirm: false,
            confirmButtonText: i18n.t('admin.components.GuildmarksManagement.deleteRow.confirmButtonText'),
            cancelButtonText: i18n.t('admin.components.GuildmarksManagement.deleteRow.cancelButtonText'),
            cancelButtonAriaLabel: "Thumbs down"
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const response = await axios.delete(`${configurationSingleton.getBaseUrl()}/admin/guildmark/${guildId}`,
                        {
                            headers: {
                                'Authorization': 'Bearer ' + localStorage.getItem('admin_jwt')
                            }
                        });
                    fetchData();
                } catch (error) {
                    Swal.fire(
                        i18n.t('admin.components.GuildmarksManagement.deleteRow.error'),
                        error.response.data.message + ' Status: ' + error.response.status + '.',
                        'error'
                    );
                }

            }
        });
    };
    const [updateInfos, setUpdateInfos] = useState(null);

    useEffect(() => {
        if (updateInfos) {
            const modalLink = document.getElementById('updateModalLink');
            modalLink.click();
        }
    }, [updateInfos]);

    const changeRow = (id, model, entries, cols) => {
        setUpdateInfos({
            modelId: id,
            model: model,
            modalEntries: entries,
            cols: cols,
            request: {
                method: 'PATCH',
                url: `${configurationSingleton.getBaseUrl()}/admin/guildmark/${id}`
            }
        });
    };
    const handleUpdateSuccess = () => {
        fetchData();
    };
    const [recordInfos, setRecordInfos] = useState(null);

    useEffect(() => {
        if (recordInfos) {
            const modalLink = document.getElementById('includeModalLink');
            modalLink.click();
        }
    }, [recordInfos]);
    const includeRow = (entries) => {
        const consistentEntries = entries.filter(n => n !== "id");
        setRecordInfos({
            modalEntries: consistentEntries,
            request: {
                method: 'POST',
                url: `${configurationSingleton.getBaseUrl()}/admin/guildmark`
            }
        });
    };

    return (
        <div className='mb-64'>
            <h2 className="text-2xl font-bold mb-3 text-white">{SUBJECT_TARGET_NAME}</h2>
            <div className="mb-2 mr-1 float-right">
                <button className='btn btn-success btn-outline'
                        onClick={() => includeRow(dynamicTableData.entriesModel)}>
                    {i18n.t('admin.components.GuildmarksManagementContent.new')}
                </button>
            </div>
            {dynamicTableData.cols.length > 0 ? (
                <table className="table table-compact  table-dark w-full">
                    <thead>
                    <tr className="text-left">
                        {dynamicTableData.cols.map((header, index) => (
                            <th key={index} className="border-b p-2">
                                {i18n.t('admin.components.GuildmarksManagementContent.columns.' + header)}
                            </th>
                        ))}

                        <th className="border-b text-center"> {i18n.t('admin.components.GuildmarksManagementContent.actions.content')}</th>
                    </tr>
                    </thead>
                    <tbody>
                    {dynamicTableData.rows.map((row, rowIndex) => (
                        <tr key={rowIndex} className='bg-gray-100'>

                            {dynamicTableData.cols.map((col, colIndex) => (
                                <td key={colIndex}>
                                    {
                                        (() => {
                                            if (col.toLowerCase() === 'image') {
                                                return <img src={row.image}  className='md:w-8 sm:w-4'/>
                                            } else {
                                                const foundItem = dynamicTableData.entriesModel.find((filteredItem) =>
                                                    filteredItem.toLowerCase().includes(col.toLowerCase().split(' ')[0])
                                                );
                                                return row[foundItem];
                                            }
                                        })()
                                    }
                                </td>
                            ))}

                            {/* inicio das ações */}
                            <td className='flex items-center justify-center'>
                                <button className="btn btn-primary ml-2"
                                        onClick={() => changeRow(row['guildId'], row, dynamicTableData.entriesModel, dynamicTableData.cols)}>
                                    {i18n.t('admin.components.GuildmarksManagementContent.actions.items.change')}
                                </button>
                                <button className="btn btn-error ml-2 text-white"
                                        onClick={() => deleteRow(row['guildId'])}>
                                    {i18n.t('admin.components.GuildmarksManagementContent.actions.items.delete')}
                                </button>
                            </td>

                            {/* fim das ações */}

                        </tr>
                    ))}

                    </tbody>


                </table>


            ) : (
                <Loading/>
            )}

            <a id="updateModalLink" href="#modal_update" style={{display: 'none'}}>Modal Link</a>
            <div className="modal" id="modal_update" style={{display: updateInfos !== undefined ? '' : 'none'}}>

                <DynamicFormModal
                    onChangeSuccess={() => handleUpdateSuccess()}
                    updateInfos={updateInfos}
                    large={true}
                />
            </div>

            <a id="includeModalLink" href="#modal_include" style={{display: 'none'}}>Modal Link</a>
            <div className="modal" id="modal_include" style={{display: updateInfos !== undefined ? '' : 'none'}}>

                <DynamicFormModal
                    onChangeSuccess={() => handleUpdateSuccess()}
                    updateInfos={recordInfos}
                    large={true}
                />
            </div>
        </div>
    );
}

export default GuildmarksManagementContent;
