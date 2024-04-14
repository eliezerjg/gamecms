import React, {useEffect, useRef, useState} from 'react';
import './styles.css';
import axios from "axios";
import Swal from "sweetalert2";
import RequestValidator from "../../../middlewares/Request.jsx";
import ReactQuill from "react-quill";

function DynamicFormModal({ onChangeSuccess, updateInfos, large = false }) {
    if (!updateInfos || !updateInfos.modalEntries) {
        return null;
    }

    const [persistModel, setPersistModel] = useState(updateInfos?.model || {});
    const isNewRecord = updateInfos?.modelId !== undefined;
    const viewHash = Math.floor(Math.random() * (5 - 1 + 1)) + 1;


    const showErrorsIfFieldsIsNotFilled = (modifiedModel) => {
        let errors = '';
        updateInfos.modalEntries.forEach(entry => {
            if (entry !== 'image' && modifiedModel[entry] === undefined) {
                errors += ("<strong>campo: </strong>" + entry + " está vazio, <BR/>");
            }
        });

        if (errors.length > 0) {
            Swal.fire(
                'Não foi possível Atualizar',
                '<p class=" text-center">' + errors + '</p>',
                'error'
            );
            return true;
        }
    }

    const doIncludeOrUpdate = (modifiedModel) => {
        if (showErrorsIfFieldsIsNotFilled(modifiedModel)) {
            return;
        }

        let requestParams = {
            url: updateInfos.request.url,
            method: updateInfos.request.method,
            data: modifiedModel,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('admin_jwt')}`
            }
        };

        axios(requestParams).then(response => {
            Swal.fire(
                'Boa !',
                (isNewRecord ? 'Alterado' : 'Incluido') + ' com  sucesso.',
                'success'
            )
            onChangeSuccess();
            document.querySelector("#modal_update > div > a > button").click();
        }, response => {
            RequestValidator.verify(response, true);
            Swal.fire(
                'Não foi possível ' + (isNewRecord ? 'Atualizar' : 'Incluir'),
                'Algo ocorreu mal: ' + (response.response?.data?.message || response.message + '.'),
                'error'
            );
        });

        setPersistModel({});
        updateInfos = null;
        document.querySelector("#modal_include > div > a").click();

    };

    const mappedTypes = {
        'paid': Boolean,
        'importedByServerCustomer': Boolean,
        'value': Number,
        'username': String,
        'id': String,
        'externalReference': String,
        'guildId': String,
        'author': String,
        'image': 'Image',
        'password': String,
        'webShopReferenceCart': 'TextArea',
        'description': 'TextArea',
        'unformatedHtmlDescription': 'TextAreaNormal',
        'tipo': 'tipoNoticias',
        'data': 'data',
        'category' : 'category'
    };

    const tipoNoticias = ['NEWS', 'EVENTS', 'DOWNLOADS', 'MAINTENANCES'];
    const tipoCategory = ['GLOVES', 'PANTS', 'HELMET', 'ARMOR', 'WEAPON', 'EVENT', 'PROMOTION', 'WEEKEND', 'TEMPORARY', 'DONATE'];

    const editorRef = useRef(null);

    useEffect(() => {
        if (updateInfos && updateInfos.modelId === undefined) {
            setPersistModel({});
        }else{
            setPersistModel(updateInfos.model);
        }
    }, [updateInfos]);

    return (
        <>
            <div className={large ? "modal-box w-11/12 max-w-5xl" : "modal-box"}>
                <a href="#">
                    <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2 text-3x2">✕</button>
                </a>

                {updateInfos.modalEntries &&
                    updateInfos.modalEntries.map((entry, index) => (

                        <div key={index} className="input-container mt-4">
                            <label className="block text-white text-sm font-bold mb-2" htmlFor={entry}>
                                {entry}
                            </label>

                            <div className="w-full">
                                {(() => {
                                    switch (entry) {
                                        case 'image':
                                            return (
                                                <>
                                                    <input
                                                        type="file"
                                                        id={entry + viewHash}
                                                        name={entry + viewHash}
                                                        accept="image/*"
                                                        className="mt-2"
                                                        style={{ display: 'none' }}
                                                        onChange={(e) => {
                                                            const selectedFile = e.target.files[0];
                                                            if (selectedFile !== null) {
                                                                const maxFileSize = 1024 * 1024;
                                                                if (selectedFile.size > maxFileSize) {
                                                                    Swal.fire({
                                                                        icon: 'error',
                                                                        title: 'Erro',
                                                                        text: `O arquivo excede o tamanho máximo permitido de 1 MB.`,
                                                                    });
                                                                    return;
                                                                }

                                                                const reader = new FileReader();
                                                                reader.onloadend = (e) => {
                                                                    setPersistModel({ ...persistModel, [entry]: reader.result});
                                                                    document.getElementById('selected_label_' + entry + viewHash).innerText = 'Arquivo anexado com sucesso.';
                                                                    document.getElementById('img_' + entry + viewHash).src = reader.result;
                                                                };
                                                                reader.readAsDataURL(e.target.files[0]);
                                                            }
                                                        }}
                                                    />

                                                    <img src={(updateInfos.model !== undefined ? updateInfos.model[entry] : './teste.bmp')} className="image-form" id={'img_' + entry + viewHash} width="40" height="40" />

                                                    <div className="mt-2 w-full">
                                                        <button className="btn btn-outline" onClick={() => { document.getElementById(entry + viewHash).click() }}>Selecionar Arquivo</button>
                                                        <label className="label_form ml-4" id={'selected_label_' + entry + viewHash}>Selecionar Arquivo.</label>
                                                    </div>
                                                </>
                                            );
                                        case 'id', 'statusDetail':
                                            return (
                                                <input
                                                    className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                    id={entry + viewHash}
                                                    name={entry + viewHash}
                                                    type="text"
                                                    value={persistModel[entry] || ''}
                                                    disabled={isNewRecord}
                                                    onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}
                                                />
                                            );
                                        default:
                                            if (mappedTypes[entry] === Boolean) {
                                                return (
                                                    <>
                                                        <select name={entry+ viewHash} id={entry+ viewHash}
                                                                className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                                onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}
                                                                value={persistModel[entry].toString() || 'SELECIONE'}
                                                        >
                                                            <option value='SELECIONE'>SELECIONE UM TIPO</option>
                                                            <option value="true">Sim</option>
                                                            <option value="talse">Nao</option>
                                                        </select>
                                                    </>
                                                );
                                            } else if (mappedTypes[entry] === 'tipoNoticias') {
                                                return (
                                                    <select name={entry+ viewHash} id={entry+ viewHash}
                                                            className="select select-bordered shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                            onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}
                                                            value={persistModel[entry] || 'SELECIONE'}
                                                    >
                                                        <option value='SELECIONE'>SELECIONE UM TIPO</option>
                                                        {tipoNoticias &&
                                                            tipoNoticias.map((tipo) => (
                                                                <option key={tipo} value={tipo}>{tipo}</option>
                                                            ))
                                                        }
                                                    </select>
                                                );
                                            }else if (mappedTypes[entry] === 'category') {
                                                return (
                                                    <select name={entry+ viewHash} id={entry+ viewHash}
                                                            className="select select-bordered shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                            onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}
                                                            value={persistModel[entry] || 'SELECIONE'}
                                                    >
                                                        <option value='SELECIONE'>SELECIONE UM TIPO</option>
                                                        {tipoCategory &&
                                                            tipoCategory.map((tipo) => (
                                                                <option key={tipo} value={tipo}>{tipo}</option>
                                                            ))
                                                        }
                                                    </select>
                                                );
                                            }
                                            else if (mappedTypes[entry] === 'data') {
                                                return (
                                                    <input
                                                        className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                        id={entry+ viewHash}
                                                        name={entry+ viewHash}
                                                        type="date"
                                                        placeholder='dd/mm/yyyy'
                                                        onChange={(e) => {
                                                            let value = e.target.value;
                                                            if (value != null) {
                                                                setPersistModel({ ...persistModel, [entry]: value });
                                                            }
                                                        }}
                                                        value={persistModel[entry] ? new Date(persistModel[entry]).toISOString().split('T')[0] : ''}
                                                    />
                                                );
                                            }
                                            else if (mappedTypes[entry] === 'TextAreaNormal') {
                                                return (
                                                    <textarea className='textarea w-full border-gray-500' defaultValue={persistModel[entry]}
                                                              onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}
                                                    >

                                                    </textarea>
                                                );
                                            }
                                            else if (mappedTypes[entry] === 'TextArea') {
                                                return (
                                                <ReactQuill
                                                    theme="snow"
                                                    value={persistModel && persistModel[entry] || ''}
                                                    onChange={(value) => setPersistModel({ ...persistModel, [entry]: value })}
                                                />
                                                );
                                            }
                                            return (
                                                <input
                                                    className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline"
                                                    id={entry+ viewHash}
                                                    name={entry+ viewHash}
                                                    type={entry === "password" ? "password" : "text"}
                                                    value={persistModel[entry] || ''}
                                                    onChange={(e) => setPersistModel({ ...persistModel, [entry]: e.target.value })}

                                                />
                                            );
                                    }
                                })()}
                            </div>
                        </div>
                    ))}

                <button
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mt-4 w-full"
                    onClick={() => doIncludeOrUpdate(persistModel)}
                >
                    {isNewRecord ? 'Alterar' : 'Incluir'}
                </button>
            </div>
        </>
    );
}

export default DynamicFormModal;
