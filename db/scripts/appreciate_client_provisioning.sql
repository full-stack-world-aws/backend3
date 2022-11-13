INSERT INTO
  oauth_client_details
  (client_id,
   resource_ids,
   client_secret,
   scope,
   authorized_grant_types,
   access_token_validity,
   refresh_token_validity,
   autoapprove)
VALUES
  ('appreciate',
   'oauth2-resource',
   '$2a$12$kv/.gzF0tCYb2xvnUmF.fuhhxqgyF1CKY3RdLDhNIXih38l7nrmKW', # "appreciate" after the bcrypt (12 rounds).
   'read,write',
   'client_credentials,password,refresh_token',
   1209600, # 14 days = 336 hours.
   0, # never expires. For now.
   '.*');
