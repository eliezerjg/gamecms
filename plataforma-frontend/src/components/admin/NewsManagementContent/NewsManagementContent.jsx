import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import configurationSingleton from '../../../middlewares/Configurations.jsx';
import DynamicFormModal from "../DynamicFormModal/DynamicFormModal.jsx";
import Loading from "../../site/Loading/Loading.jsx";
import {format} from "date-fns";
import RequestValidator from "../../../middlewares/Request.jsx";
import i18n from "../../../lib/i18n.jsx";

function NewsManagementContent() {
    const [dynamicTableData, setDynamicTableData] = useState({
        cols: [],
        rows: [],
        entriesModel: []
    });
    const SUBJECT_TARGET_NAME = i18n.t('admin.components.NewsManagementContent.content');
    const GET_NOT_FOUND_MESSAGE = (TARGET = SUBJECT_TARGET_NAME) => {return 'Não foram encontradas ' + TARGET + '.'};
    const GET_NOT_FOUND_TITLE = () => {return 'Erro'};
    const fetchData = async () => {
        try {
            const response = await axios.get(`${configurationSingleton.getBaseUrl()}/admin/noticias`, {
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
    const renderImage = (base64String) => {
        if (!base64String) {
            return null;
        }

        const isBase64 = (str) => {
            try {
                return btoa(atob(str)) === str;
            } catch (err) {
                return false;
            }
        };

        if (isBase64(base64String)) {
            return <img src={`data:image/bmp;base64,${base64String}`} alt="Base64" width="50" height="50" />;
        }

        return base64String;
    };
    const deleteRow = (id) => {
        const noticiaId = id;
        Swal.fire({
            title: "<strong>"+i18n.t('admin.components.NewsManagement.deleteRow.confirm')+"</strong>",
            showCloseButton: true,
            showCancelButton: true,
            focusConfirm: false,
            confirmButtonText: i18n.t('admin.components.NewsManagement.deleteRow.confirmButtonText'),
            cancelButtonText: i18n.t('admin.components.NewsManagement.deleteRow.cancelButtonText'),
            cancelButtonAriaLabel: "Thumbs down"
        }).then(async (result) => {
            if (result.isConfirmed) {
                try{
                    const response = await axios.delete(`${configurationSingleton.getBaseUrl()}/admin/noticia/${noticiaId}`,
                        {
                            headers: {
                                'Authorization': 'Bearer ' + localStorage.getItem('admin_jwt')
                            }
                        });
                    window.location.reload();
                }catch (error) {
                    Swal.fire(
                        i18n.t('admin.components.NewsManagement.deleteRow.error'),
                        error.response.data.message + ' Status: ' + error.response.status + '.',
                        'error'
                    );
                }

            }
        });
    };
    const [updateInfos, setUpdateInfos] = useState(null);
    const changeRow = (id, model, entries, cols) => {
        setUpdateInfos({
            modelId: id,
            model: model,
            modalEntries: entries,
            cols : cols,
            request: {
                method : 'PATCH',
                url : `${configurationSingleton.getBaseUrl()}/admin/noticia/${id}`
            }
        });

        console.log(updateInfos);

        const modalLink = document.getElementById('updateModalLink');
        modalLink.click();
    };

    const [recordInfos, setRecordInfos] = useState(null);
    const includeRow = (entries) => {
        const consistentEntries = entries.filter(n => n !== "id");
        setRecordInfos({
            modalEntries: consistentEntries,
            request: {
                method : 'POST',
                url : `${configurationSingleton.getBaseUrl()}/admin/noticia`
            }
        });

        const modalLink = document.getElementById('includeModalLink');
        modalLink.click();
    };
    const handleUpdateSuccess = () => {
        fetchData();
    };

    return (
        <div className='mb-64'>
            <h2 className="text-2xl font-bold mb-3 text-white">{SUBJECT_TARGET_NAME}</h2>

            <div className="mb-2 mr-1 float-right">
                <button className='btn btn-success btn-outline' onClick={() => includeRow(dynamicTableData.entriesModel)}>
                    {i18n.t('admin.components.NewsManagementContent.new')}
                </button>
            </div>

            {dynamicTableData.cols.length > 0 ? (
                <table className="table table-compact table-dark w-full">
                    <thead>
                    <tr className="text-left">
                        {dynamicTableData.cols.map((header, index) => (
                            <th key={index} className="border-b">
                                {i18n.t('admin.components.NewsManagementContent.columns.' + header)}
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
                                                return renderImage(row.image);
                                            }else if (col.toLowerCase() === 'paid') {
                                                return row[col].toString() === 'true' ? 'Pago' : 'Nao pago';
                                            }else if (col.toLowerCase() === 'data') {
                                                return format(new Date(row[col.toLowerCase()]), 'dd/MM/yyyy');
                                            }else if (col.toLowerCase() === 'id') {
                                                return row[col.toLowerCase()];
                                            }
                                            else {
                                                const value = row[col.toLowerCase()];

                                                return (value && typeof value === 'string' && value.length > 20)
                                                    ? `${value.replace(/<\/?[^>]*>/g, "").substring(0, 20)}...`
                                                    : (value && typeof value === 'string')
                                                        ? value.replace(/<\/?[^>]*>/g, "")
                                                        : '';

                                            }
                                        })()
                                    }
                                </td>
                            ))}

                            {/* inicio das ações */}
                            <td className='flex items-center justify-center'>
                                <button className="btn btn-info ml-2" onClick={() => {window.open(`/post/${row['id']}`, '_blank');}}>
                                    {i18n.t('admin.components.NewsManagementContent.actions.items.preview')}
                                </button>
                                <button className="btn btn-primary ml-2" onClick={() => changeRow(row['id'], row, dynamicTableData.entriesModel, dynamicTableData.cols)}>
                                    {i18n.t('admin.components.NewsManagementContent.actions.items.change')}
                                </button>
                                <button className="btn btn-error ml-2 text-white" onClick={() => deleteRow(row['id'])}>
                                    {i18n.t('admin.components.NewsManagementContent.actions.items.delete')}
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

            <a id="updateModalLink" href="#modal_update" style={{ display: 'none' }}>Modal Link</a>
            <div className="modal" id="modal_update">

                <DynamicFormModal
                    onChangeSuccess={() => handleUpdateSuccess()}
                    updateInfos={updateInfos}
                    large={true}
                />
            </div>

            <a id="includeModalLink" href="#modal_include" style={{ display: 'none' }}>Modal Link</a>
            <div className="modal" id="modal_include">

                <DynamicFormModal
                    onChangeSuccess={() => handleUpdateSuccess()}
                    updateInfos={recordInfos}
                    large={true}
                />
            </div>


        </div>
    );
}

export default NewsManagementContent;
