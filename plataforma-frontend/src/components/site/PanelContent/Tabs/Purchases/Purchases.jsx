import React, { useEffect, useState } from 'react';
import './styles.css';
import axios from 'axios';
import Configurations from '../../../../../middlewares/Configurations.jsx';
import Loading from "../../../Loading/Loading.jsx";
import i18n from "../../../../../lib/i18n.jsx"

function Purchases() {
    const [tableData, setTableData] = useState(null);
    const [loading, setLoading] = useState(true);
    const username = localStorage.getItem('username');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(
                    Configurations.getBaseUrl() + '/purchases/user/' + username
                );
                const data = response.data;
                setTableData(data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching data:', error);
                setLoading(false);
            }
        };

        fetchData();
    }, [username]);

    return (
        <div>
            <h1 className="text-2xl text-center font-semibold mb-4 text-white">
                {i18n.t('site.components.PanelContent.tabs.purchases.content')}
            </h1>

            {loading ? (
                <div className='container mx-auto mt-24'>
                    <Loading />
                </div>
            ) : (
                <table className="table table-compact table-zebra table-dark w-full">
                    <thead>
                    <tr className='text-center'>
                        <th>{i18n.t('site.components.PanelContent.tabs.purchases.head.line')}</th>
                        <th>{i18n.t('site.components.PanelContent.tabs.purchases.head.reference')}</th>
                        <th>{i18n.t('site.components.PanelContent.tabs.purchases.head.value')}</th>
                        <th>{i18n.t('site.components.PanelContent.tabs.purchases.head.recepted')}</th>
                    </tr>
                    </thead>
                    <tbody>
                    {tableData &&
                        tableData.map((item, index) => (
                            <tr key={item.id} className='text-center'>
                                <td>{index + 1}</td>
                                <td>{item.externalReference}</td>
                                <td>
                                    {new Intl.NumberFormat('pt-BR', {
                                        style: 'currency',
                                        currency: Configurations.getCurrency()["name"],
                                    }).format(item.value)}
                                </td>
                                <td>{item.imported ? i18n.t('site.components.PanelContent.tabs.purchases.words.yes') : i18n.t('site.components.PanelContent.tabs.purchases.words.no')}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default Purchases;
