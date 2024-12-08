Для реализации предложенного сценария с использованием Apache JMeter и проверки корректности изоляции транзакций в системе, следуем следующим этапам:

1. Общая структура тестирования:
Тестовые сценарии:

Импорт объектов.
Создание объектов (с проверкой уникальности).
Редактирование объектов (с проверкой одновременных изменений).
Удаление объектов (с проверкой одновременных удалений).
Моделирование пользователей:

Используем Thread Groups в JMeter для имитации множества пользователей.
Настраиваем нагрузку: от 10 до 100 пользователей, работающих параллельно.
Цели:

Проверить корректность выполнения операций при одновременных запросах.
Проверить соблюдение транзакционной изоляции.
Проверить выполнение ограничений уникальности.
2. Настройка Apache JMeter для каждого сценария
2.1 Импорт объектов
Thread Group:

Установить количество потоков = количеству пользователей.
Задать Ramp-Up Period (например, 5 секунд).
Повторить выполнение для каждого пользователя.
HTTP Request:

Endpoint: /import (или другой доступный маршрут импорта).
Тип: POST.
Тело запроса: JSON-файл с драконами.
Ответы:

Проверить, что операции атомарны (если одна транзакция неудачна, изменения отсутствуют).
Проверить статус ответа (200 OK или 500 Internal Server Error).
2.2 Создание объектов (с проверкой уникальности)
Thread Group:

Количество потоков: 10-50.
Операции выполняются параллельно.
HTTP Request:

Endpoint: /dragons (или /objects/create).
Тип: POST.
Тело запроса: JSON с одним и тем же уникальным значением (например, name или id).
Ответы:

Проверить, что только одна транзакция успешно создала объект.
Остальные запросы должны вернуть ошибку (например, 409 Conflict).
2.3 Редактирование объектов (с проверкой одновременных изменений)
Thread Group:

Два потока (пользователя).
Оба пользователя пытаются изменить один и тот же объект одновременно.
HTTP Request:

Endpoint: /dragons/{id}.
Тип: PUT.
Тело запроса: JSON с изменениями (разные значения для обоих пользователей).
Ответы:

Проверить, что только одна транзакция была успешно применена.
Если используется уровень изоляции REPEATABLE READ, проверить, что изменения не повлияли на результат параллельных транзакций.
2.4 Удаление объектов (с проверкой одновременных удалений)
Thread Group:

Два потока (пользователя).
Оба пользователя пытаются удалить один и тот же объект одновременно.
HTTP Request:

Endpoint: /dragons/{id}.
Тип: DELETE.
Ответы:

Проверить, что только одна транзакция успешно завершена (статус 200 OK).
Вторая должна вернуть ошибку (например, 404 Not Found).
3. Анализ результатов
Логирование:

Настроить View Results Tree и Aggregate Report в JMeter для сбора результатов запросов.
Сохранять лог запросов и ответов для анализа транзакционной целостности.
Анализ изоляции:

Использовать уровень изоляции READ COMMITTED как начальный.
Если при параллельных редактированиях/удалениях обнаруживаются проблемы (например, "dirty reads", "lost updates"):
Поднять уровень изоляции до REPEATABLE READ или SERIALIZABLE.
Ограничения уникальности:

Проверить соблюдение ограничений уникальности в базе данных.
В случае ошибок настроить оптимистичную блокировку (использование уникальных индексов).
4. Внесение изменений в уровень изоляции
Рекомендуемые изменения:
Импорт данных:

Установить уровень изоляции: SERIALIZABLE.
Обеспечивает полное исключение проблем конкурентности, гарантируя последовательное выполнение транзакций.
Создание объектов:

Использовать уникальные индексы в базе данных.
Транзакционный уровень: REPEATABLE READ.
Редактирование объектов:

Установить уровень изоляции: READ COMMITTED.
Если обнаружены "потерянные изменения", поднять до REPEATABLE READ.
Удаление объектов:

Уровень изоляции: REPEATABLE READ.
Исключает проблемы "потерянных обновлений".
5. Итоговый JMeter сценарий
Файл JMeter (JMX) должен содержать:

Thread Groups для каждого сценария.
HTTP Request Samplers с параметрами для каждой операции.
Listeners для логирования и анализа.
Конфигурацию, позволяющую изменять количество потоков и параметры запросов.
Заключение
Проведение тестов с JMeter позволяет выявить потенциальные проблемы конкурентности и ограничений уникальности. После анализа результатов можно обосновать повышение уровня изоляции транзакций там, где это необходимо, и подтвердить корректность работы системы при многопользовательском доступе.

Below is the structure and content of a JMeter Test Plan for testing transactional integrity and concurrency in your Spring application. The plan covers import, create, edit, and delete operations, along with testing unique constraints and concurrent access.

JMeter Test Plan (JMX)
Here is the generated JMX content in XML format:

<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0" jmeter="5.5">
  <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="Transactional Test Plan" enabled="true">
    <stringProp name="TestPlan.comments">Test Plan for Transaction Isolation and Concurrency</stringProp>
    <boolProp name="TestPlan.functional_mode">false</boolProp>
    <boolProp name="TestPlan.tearDown_on_shutdown">true</boolProp>
    <elementProp name="TestPlan.user_defined_variables" elementType="Arguments" guiclass="ArgumentsPanel" testclass="Arguments" testname="User Defined Variables" enabled="true">
      <collectionProp name="Arguments.arguments">
        <elementProp name="baseUrl" elementType="Argument">
          <stringProp name="Argument.name">baseUrl</stringProp>
          <stringProp name="Argument.value">http://localhost:8080</stringProp>
          <stringProp name="Argument.metadata">=</stringProp>
        </elementProp>
      </collectionProp>
    </elementProp>
    <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
  </TestPlan>
  <hashTree>
    <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Import Thread Group" enabled="true">
      <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
      <elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControllerGui" testclass="LoopController" testname="Loop Controller" enabled="true">
        <boolProp name="LoopController.continue_forever">false</boolProp>
        <stringProp name="LoopController.loops">1</stringProp>
      </elementProp>
      <stringProp name="ThreadGroup.num_threads">20</stringProp>
      <stringProp name="ThreadGroup.ramp_time">10</stringProp>
      <boolProp name="ThreadGroup.scheduler">false</boolProp>
    </ThreadGroup>
    <hashTree>
      <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="Import Dragons" enabled="true">
        <stringProp name="HTTPSampler.domain">${baseUrl}</stringProp>
        <stringProp name="HTTPSampler.port"></stringProp>
        <stringProp name="HTTPSampler.protocol">http</stringProp>
        <stringProp name="HTTPSampler.path">/import</stringProp>
        <stringProp name="HTTPSampler.method">POST</stringProp>
        <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
        <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
        <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
        <boolProp name="HTTPSampler.DO_MULTIPART_POST">true</boolProp>
        <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
          <collectionProp name="Arguments.arguments">
            <elementProp name="file" elementType="HTTPFileArg">
              <stringProp name="HTTPFileArg.path">dragons.json</stringProp>
              <stringProp name="HTTPFileArg.paramname">file</stringProp>
              <stringProp name="HTTPFileArg.mimetype">application/json</stringProp>
            </elementProp>
          </collectionProp>
        </elementProp>
      </HTTPSamplerProxy>
      <hashTree>
        <ResponseAssertion guiclass="AssertionGui" testclass="ResponseAssertion" testname="Check Import Response" enabled="true">
          <collectionProp name="Asserion.test_strings">
            <stringProp name="test_string">200</stringProp>
          </collectionProp>
          <stringProp name="Assertion.custom_message">Expected HTTP 200</stringProp>
          <boolProp name="Assertion.test_field">true</boolProp>
          <stringProp name="Assertion.test_field">Assertion.response_code</stringProp>
          <boolProp name="Assertion.assume_success">false</boolProp>
          <intProp name="Assertion.test_type">2</intProp>
        </ResponseAssertion>
      </hashTree>
    </hashTree>
    <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Create Thread Group" enabled="true">
      <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
      <elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControllerGui" testclass="LoopController" testname="Loop Controller" enabled="true">
        <boolProp name="LoopController.continue_forever">false</boolProp>
        <stringProp name="LoopController.loops">10</stringProp>
      </elementProp>
      <stringProp name="ThreadGroup.num_threads">50</stringProp>
      <stringProp name="ThreadGroup.ramp_time">5</stringProp>
      <boolProp name="ThreadGroup.scheduler">false</boolProp>
    </ThreadGroup>
    <hashTree>
      <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="Create Dragon" enabled="true">
        <stringProp name="HTTPSampler.domain">${baseUrl}</stringProp>
        <stringProp name="HTTPSampler.port"></stringProp>
        <stringProp name="HTTPSampler.protocol">http</stringProp>
        <stringProp name="HTTPSampler.path">/dragons</stringProp>
        <stringProp name="HTTPSampler.method">POST</stringProp>
        <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
        <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
        <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
        <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
          <collectionProp name="Arguments.arguments">
            <elementProp name="uniqueField" elementType="Argument">
              <stringProp name="Argument.name">name</stringProp>
              <stringProp name="Argument.value">TestDragon</stringProp>
              <stringProp name="Argument.metadata">=</stringProp>
            </elementProp>
          </collectionProp>
        </elementProp>
      </HTTPSamplerProxy>
      <hashTree>
        <ResponseAssertion guiclass="AssertionGui" testclass="ResponseAssertion" testname="Check Create Response" enabled="true">
          <collectionProp name="Asserion.test_strings">
            <stringProp name="test_string">201</stringProp>
          </collectionProp>
          <stringProp name="Assertion.custom_message">Expected HTTP 201</stringProp>
          <boolProp name="Assertion.test_field">true</boolProp>
          <stringProp name="Assertion.test_field">Assertion.response_code</stringProp>
          <boolProp name="Assertion.assume_success">false</boolProp>
          <intProp name="Assertion.test_type">2</intProp>
        </ResponseAssertion>
      </hashTree>
    </hashTree>
    <!-- Additional ThreadGroups for Edit/Delete operations can be added in the same format -->
  </hashTree>
</jmeterTestPlan>
展开
Instructions for Use
Save the file:

Copy the XML content into a file named transactional_test_plan.jmx.
Add dependencies:

Ensure dragons.json is placed in the working directory or update the path in the file argument.
Run the Test Plan:

Open the file in Apache JMeter.
Configure the baseUrl variable (e.g., http://localhost:8080).
Start the test and analyze results using listeners like View Results Tree and Aggregate Report.
Extend as needed:

Add Thread Groups for "Edit" and "Delete" following the same pattern.
Update assertions to validate the specific requirements.
This test plan will evaluate the concurrent behavior of your system and help verify transactional isolation in your Spring application.