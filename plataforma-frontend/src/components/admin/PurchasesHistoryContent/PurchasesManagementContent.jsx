import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import configurationSingleton from '../../../middlewares/Configurations.jsx';
import DynamicFormModal from "../DynamicFormModal/DynamicFormModal.jsx";
import Loading from "../../site/Loading/Loading.jsx";
import RequestValidator from "../../../middlewares/Request.jsx";
import {format} from "date-fns";
import i18n from "../../../lib/i18n.jsx";

function PurchasesManagementContent() {
    const [dynamicTableData, setDynamicTableData] = useState({
        cols: [],
        rows: [],
        entriesModel: []
    });
    const SUBJECT_TARGET_NAME = i18n.t('admin.components.PurchasesManagementContent.content');
    const GET_NOT_FOUND_MESSAGE = (TARGET = SUBJECT_TARGET_NAME) => i18n.t('admin.components.PurchasesManagementContent.GET_NOT_FOUND_MESSAGE.content');
    const GET_NOT_FOUND_TITLE = () => i18n.t('admin.components.PurchasesManagementContent.GET_NOT_FOUND_TITLE.content');
    const fetchData = async () => {
        try {
            const response = await axios.get(`${configurationSingleton.getBaseUrl()}/admin/purchases`, {
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
            return <img src={`data:image/bmp;base64,${base64String}`} alt="Base64" width="50" height="50"/>;
        }

        return base64String;
    };
    const deleteRow = (id) => {
        const compraId = id;
        Swal.fire({
            title: "<strong>"+i18n.t('admin.components.PurchasesManagement.deleteRow.confirm')+"</strong>",
            showCloseButton: true,
            showCancelButton: true,
            focusConfirm: false,
            confirmButtonText: i18n.t('admin.components.PurchasesManagement.deleteRow.confirmButtonText'),
            cancelButtonText: i18n.t('admin.components.PurchasesManagement.deleteRow.cancelButtonText'),
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const response = await axios.delete(`${configurationSingleton.getBaseUrl()}/admin/purchase/${compraId}`,
                        {
                            headers: {
                                'Authorization': 'Bearer ' + localStorage.getItem('admin_jwt')
                            }
                        });
                    fetchData();
                } catch (error) {
                    Swal.fire(
                        i18n.t('admin.components.PurchasesManagement.deleteRow.error'),
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
            cols: cols,
            request: {
                method: 'PATCH',
                url: `${configurationSingleton.getBaseUrl()}/admin/purchase/${id}`
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
                url : `${configurationSingleton.getBaseUrl()}/admin/purchase`
            }
        });

        const modalLink = document.getElementById('includeModalLink');
        modalLink.click();
    };
    const handleUpdateSuccess = () => {
        fetchData();
    };

    return (
        <div className="w-full mb-64">
            <h2 className="text-2xl font-bold mb-3 text-white">{SUBJECT_TARGET_NAME}
            </h2>
            <div className="mb-2 mr-1 float-right">
                <button className='btn btn-success btn-outline' onClick={() => includeRow(dynamicTableData.entriesModel)} style={{ display: 'none' }}>
                    {i18n.t('admin.components.PurchasesManagementContent.new')}
                </button>
            </div>
            {dynamicTableData.cols.length > 0 ? (
                <table className="table table-compact  table-dark w-full">
                    <thead>
                    <tr className="text-left">
                        {dynamicTableData.cols.map((header, index) => (
                            <th key={index} className="border-b">
                                {i18n.t('admin.components.PurchasesManagementContent.columns.' + header)}
                            </th>
                        ))}

                        <th className="border-b text-center"> {i18n.t('admin.components.PurchasesManagementContent.actions.content')}</th>

                    </tr>
                    </thead>
                    <tbody>
                    {dynamicTableData.rows.map((row, rowIndex) => (
                        <tr key={rowIndex} className='bg-gray-100'>

                            {dynamicTableData.cols.map((col, colIndex) => (
                                <td className='text-left' key={colIndex}>
                                    {
                                        (() => {
                                            if (row[col] instanceof Boolean || col  === 'paid' || col  === 'importedbyservercustomer') {
                                                return row[col] ? <button className='btn bg-green-800'> V </button> : <button className='btn bg-red-800'> X </button>;
                                            }else if(col === 'referenceinpaymentmethod'){
                                                return row['referenceInPaymentMethod'];
                                            }
                                            else if(col === 'dateinc'){
                                                return row['dateInc'] || '-';
                                            }
                                            else{
                                                return row[col];
                                            }
                                        })()
                                    }
                                </td>
                            ))}

                            {/* inicio das ações */}
                            <td className='flex items-center justify-center'>
                                <button className="btn btn-primary ml-2"
                                        onClick={() => changeRow(row['id'], row, dynamicTableData.entriesModel, dynamicTableData.cols)}>
                                    {i18n.t('admin.components.PurchasesManagementContent.actions.items.change')}
                                </button>
                                <button className="btn btn-error ml-2 text-white"
                                        onClick={() => deleteRow(row['id'])}>
                                    {i18n.t('admin.components.PurchasesManagementContent.actions.items.delete')}
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

export default PurchasesManagementContent;
