create table if nsot exists Requests(
    id identity primary key,
    url text,
    method text,
    headers text,
    queries text,
    body text
);